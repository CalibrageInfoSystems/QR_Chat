package com.cis.qrchat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sendmoneyrequestobject {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userWalletHistory")
    @Expose
    private UserWalletHistory userWalletHistory;
    @SerializedName("recieverUserName")
    @Expose
    private String recieverUserName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserWalletHistory getUserWalletHistory() {
        return userWalletHistory;
    }

    public void setUserWalletHistory(UserWalletHistory userWalletHistory) {
        this.userWalletHistory = userWalletHistory;
    }

    public String getRecieverUserName() {
        return recieverUserName;
    }

    public void setRecieverUserName(String recieverUserName) {
        this.recieverUserName = recieverUserName;
    }



public class UserWalletHistory {

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
    @SerializedName("groupId")
    @Expose
    private Object groupId;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

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

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }}}