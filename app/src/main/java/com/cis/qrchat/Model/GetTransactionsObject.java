package com.cis.qrchat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransactionsObject {

    @SerializedName("loginUserWalletId")
    @Expose
    private String loginUserWalletId;
    @SerializedName("contactUserWalletId")
    @Expose
    private String contactUserWalletId;

    public String getLoginUserWalletId() {
        return loginUserWalletId;
    }

    public void setLoginUserWalletId(String loginUserWalletId) {
        this.loginUserWalletId = loginUserWalletId;
    }

    public String getContactUserWalletId() {
        return contactUserWalletId;
    }

    public void setContactUserWalletId(String contactUserWalletId) {
        this.contactUserWalletId = contactUserWalletId;
    }

}