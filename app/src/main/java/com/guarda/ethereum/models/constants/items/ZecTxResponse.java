package com.guarda.ethereum.models.constants.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.guarda.ethereum.models.items.OutputDescs;
import com.guarda.ethereum.models.items.SpendDescs;
import com.guarda.ethereum.models.items.Vin;
import com.guarda.ethereum.models.items.Vjoinsplit;
import com.guarda.ethereum.models.items.Vout;

import java.util.List;

public class ZecTxResponse {

    @SerializedName("txid")
    @Expose
    private String txid;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("locktime")
    @Expose
    private Integer locktime;
    @SerializedName("vin")
    @Expose
    private List<Vin> vin;
    @SerializedName("vout")
    @Expose
    private List<Vout> vout;
    @SerializedName("vjoinsplit")
    @Expose
    private List<Vjoinsplit> vjoinsplit;
    @SerializedName("blockhash")
    @Expose
    private String blockhash;
    @SerializedName("blockheight")
    @Expose
    private Integer blockheight;
    @SerializedName("confirmations")
    @Expose
    private Integer confirmations;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("blocktime")
    @Expose
    private Integer blocktime;
    @SerializedName("valueOut")
    @Expose
    private Double valueOut;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("valueIn")
    @Expose
    private Double valueIn;
    @SerializedName("fees")
    @Expose
    private Double fees;
    @SerializedName("outputDescs")
    @Expose
    private List<OutputDescs> outputDescs;
    @SerializedName("spendDescs")
    @Expose
    private List<SpendDescs> spendDescs;

    public String getHash() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getLocktime() {
        return locktime;
    }

    public void setLocktime(Integer locktime) {
        this.locktime = locktime;
    }

    public List<Vin> getVin() {
        return vin;
    }

    public void setVin(List<Vin> vin) {
        this.vin = vin;
    }

    public List<Vout> getVout() {
        return vout;
    }

    public void setVout(List<Vout> vout) {
        this.vout = vout;
    }

    public List<Vjoinsplit> getVjoinsplit() {
        return vjoinsplit;
    }

    public void setVjoinsplit(List<Vjoinsplit> vjoinsplit) {
        this.vjoinsplit = vjoinsplit;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public Integer getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(Integer blockheight) {
        this.blockheight = blockheight;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(Integer blocktime) {
        this.blocktime = blocktime;
    }

    public Double getValueOut() {
        return valueOut;
    }

    public void setValueOut(Double valueOut) {
        this.valueOut = valueOut;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Double getValueIn() {
        return valueIn;
    }

    public void setValueIn(Double valueIn) {
        this.valueIn = valueIn;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public List<OutputDescs> getOutputDescs() {
        return outputDescs;
    }

    public List<SpendDescs> getSpendDescs() {
        return spendDescs;
    }

    @Override
    public String toString() {
        return "ZecTxResponse{" +
                "txid='" + txid + '\'' +
                ", version=" + version +
                ", locktime=" + locktime +
                ", vin=" + vin +
                ", vout=" + vout +
                ", vjoinsplit=" + vjoinsplit +
                ", blockhash='" + blockhash + '\'' +
                ", blockheight=" + blockheight +
                ", confirmations=" + confirmations +
                ", time=" + time +
                ", blocktime=" + blocktime +
                ", valueOut=" + valueOut +
                ", size=" + size +
                ", valueIn=" + valueIn +
                ", fees=" + fees +
                ", outputDescs=" + outputDescs +
                ", spendDescs=" + spendDescs +
                '}';
    }
}