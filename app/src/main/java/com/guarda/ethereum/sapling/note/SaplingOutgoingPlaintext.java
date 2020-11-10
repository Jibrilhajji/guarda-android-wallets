package com.guarda.ethereum.sapling.note;

import com.google.common.primitives.Bytes;

import work.samosudov.rustlib.RustAPI;


public class SaplingOutgoingPlaintext {

    public byte[] pk_d;
    public byte[] esk;

    public SaplingOutgoingPlaintext(byte[] pk_d, byte[] esk) {
        this.pk_d = pk_d;
        this.esk = esk;
    }

    public static byte[] encryptToOurselves(byte[] ovk, byte[] cv, byte[] cm,  byte[] epk, byte[] message) {
        byte[] K = RustAPI.prfOck(ovk, cv, cm, epk);

        byte[] sec = RustAPI.encryptOutgoing(K, message);

        return sec;
    }

    /**
     * pk_d - 8 bytes
     * esk - 8 bytes
     */
    public byte[] toByte() {
        byte[] bytes = new byte[0];
        return Bytes.concat(bytes, this.pk_d, this.esk);
    }
}
