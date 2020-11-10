package com.guarda.ethereum.sapling.db.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "details_tx")
public class DetailsTxRoom {

    @PrimaryKey
    @NonNull
    private String hash;
    private Long time;
    private Long sum;
    private Boolean isReceived;
    private Long confirmations;
    private String from;
    private String to;
    private Boolean isOut;

    public DetailsTxRoom(@NonNull String hash, Long time, Long sum, Boolean isReceived, Long confirmations, String from, String to, Boolean isOut) {
        this.hash = hash;
        this.time = time;
        this.sum = sum;
        this.isReceived = isReceived;
        this.confirmations = confirmations;
        this.from = from;
        this.to = to;
        this.isOut = isOut;
    }

    @NonNull
    public String getHash() {
        return hash;
    }

    public Long getTime() {
        return time;
    }

    public Long getSum() {
        return sum;
    }

    public Boolean getReceived() {
        return isReceived;
    }

    public Long getConfirmations() {
        return confirmations;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Boolean getOut() {
        return isOut;
    }
}
