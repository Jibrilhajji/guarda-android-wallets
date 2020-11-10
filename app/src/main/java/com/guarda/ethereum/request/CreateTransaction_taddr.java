package com.guarda.ethereum.request;

import com.guarda.ethereum.WalletCallback;
import com.guarda.ethereum.ZCashException;
import com.guarda.ethereum.ZCashTransactionOutput;
import com.guarda.ethereum.ZCashTransaction_taddr;
import com.guarda.ethereum.crypto.DumpedPrivateKey;

import java.util.LinkedList;
import java.util.List;

public class CreateTransaction_taddr extends AbstractZCashRequest implements Runnable {
  private String fromAddr;
  private String toAddr;
  private String privateKey;
  private WalletCallback<String, ZCashTransaction_taddr> callback;
  private long fee;
  private long value;
  private List<ZCashTransactionOutput> utxos;
  private int expiryHeight;

  public CreateTransaction_taddr(String fromAddr,
                                 String toAddr,
                                 long value,
                                 long fee,
                                 String privatekey,
                                 int expiryHeight,
                                 WalletCallback<String, ZCashTransaction_taddr> callback,
                                 List<ZCashTransactionOutput> utxos) {
    this.fromAddr = fromAddr;
    this.toAddr = toAddr;
    this.value = value;
    this.fee = fee;
    this.privateKey = privatekey;
    this.callback = callback;
    this.utxos = utxos;
    this.expiryHeight = expiryHeight;
  }

  @Override
  public void run() {
    try {
      ZCashTransaction_taddr tx = createTransaction();
      callback.onResponse("ok", tx);
    } catch (ZCashException e) {
      callback.onResponse(e.getMessage(), null);
    }
  }

  private ZCashTransaction_taddr createTransaction() throws ZCashException {
    List<ZCashTransactionOutput> outputs = new LinkedList<>();
    long realValue = chooseUTXOs(outputs);
    if (realValue < fee + value) {
      throw new ZCashException("Not enough balance.");
    }

    return new ZCashTransaction_taddr(DumpedPrivateKey.fromBase58(privateKey), fromAddr, toAddr,
            value, fee, expiryHeight, outputs);
  }


  private long chooseUTXOs(List<ZCashTransactionOutput> outputs) {
    long realValue = value + fee;
    long sum = 0;
    for (ZCashTransactionOutput out : utxos) {
      outputs.add(out);
      sum += out.value;
      if (sum >= realValue) {
        break;
      }

    }

    return sum;
  }
}
