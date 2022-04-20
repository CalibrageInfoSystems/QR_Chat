package com.cis.qrchat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMoneytoUserWalletRequest {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("cardId")
    @Expose
    private String cardId;
    @SerializedName("groupId")
    @Expose
    private Object groupId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("walletId")
    @Expose
    private String walletId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("transactionTypeId")
    @Expose
    private Integer transactionTypeId;
    @SerializedName("reasonTypeId")
    @Expose
    private Integer reasonTypeId;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("currency")
    @Expose
    private String currency;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public Integer getReasonTypeId() {
        return reasonTypeId;
    }

    public void setReasonTypeId(Integer reasonTypeId) {
        this.reasonTypeId = reasonTypeId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
