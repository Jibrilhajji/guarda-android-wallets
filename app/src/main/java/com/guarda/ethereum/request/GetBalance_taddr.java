package com.guarda.ethereum.request;

import android.net.Uri;

import com.guarda.ethereum.BuildConfig;
import com.guarda.ethereum.WalletCallback;
import com.guarda.ethereum.ZCashException;
import com.guarda.ethereum.ZCashTransactionDetails_taddr;
import com.guarda.ethereum.ZCashTransactionInput;
import com.guarda.ethereum.ZCashTransactionOutput;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import static com.guarda.ethereum.models.constants.Const.MAINNET_FLAVOR;
import static com.guarda.ethereum.models.constants.ZecExplorer.ZEC_EXPLORER_API;
import static com.guarda.ethereum.models.constants.ZecExplorer.ZEC_EXPLORER_API_TESTNET;

public class GetBalance_taddr extends AbstractZCashRequest implements Runnable {

  private String pubKey;
  private WalletCallback<String, Long> onComplete;
  private Vector<ZCashTransactionDetails_taddr> cache;

  public GetBalance_taddr(String pubKey, Vector<ZCashTransactionDetails_taddr> cache, WalletCallback<String, Long> onComplete) {
    this.pubKey = pubKey;
    this.cache = cache;
    this.onComplete = onComplete;
  }


  private long calcBalance() {
    synchronized (cache) {
      HashSet<ZCashTransactionOutput> outs = new HashSet<>();
      for (ZCashTransactionDetails_taddr details : cache) {
        addOuts(outs, details.vin, pubKey);
        addOuts(outs, details.vout, pubKey);
      }

      long sum = 0;
      for (ZCashTransactionOutput out : outs) {
        if (!(out instanceof ZCashTransactionInput)) {
          sum += out.value;
        }
      }

      return sum;
    }
  }

  private void addOuts(HashSet<ZCashTransactionOutput> outsCache, List<ZCashTransactionOutput> txouts, String pubKey) {
    for (ZCashTransactionOutput out : txouts) {
      if (out.address != null && out.address.equals(pubKey)) {
        if (outsCache.contains(out)) {
          outsCache.remove(out);
        } else {
          outsCache.add(out);
        }
      }
    }
  }

  private long getBalance() throws ZCashException {
    String url;
    if (BuildConfig.FLAVOR.equals(MAINNET_FLAVOR)) {
      url = ZEC_EXPLORER_API;
    } else {
      url = ZEC_EXPLORER_API_TESTNET;
    }
    String uri = Uri.parse(url).buildUpon()
            .appendEncodedPath("addr")
            .appendEncodedPath(pubKey)
            .appendEncodedPath("balance")
            .build().toString();
    String response = getResponseString(queryExplorerForConnection(uri));
    long balance;
    try {
      balance = Long.parseLong(response.substring(0, response.length() - 1));
    } catch (NumberFormatException e) {
      throw new ZCashException(String.format("Cannot parse balance from %s", uri), e);
    }

    return balance;
  }

  @Override
  public void run() {
    try {
      long balance = getBalance();
      onComplete.onResponse("ok", balance);
    } catch (ZCashException e) {
      onComplete.onResponse(e.getMessage(), null);
    }
  }
}
