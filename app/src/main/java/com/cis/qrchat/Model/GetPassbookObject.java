package com.cis.qrchat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPassbookObject {

    @SerializedName("walletId")
    @Expose
    private String walletId;
    @SerializedName("transactionTypeId")
    @Expose
    private Object transactionTypeId;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Object getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Object transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

}