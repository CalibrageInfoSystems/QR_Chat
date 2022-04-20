package com.cis.qrchat.Model;

public class Transactiondata {
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    private Double amount;
    private Integer transactionTypeId;
    private String name;
    private String mobileNumber;
    private String userImage;

    public Transactiondata(Double amount, Integer transactionTypeId, String name, String mobileNumber, String userImage) {
        this.amount = amount;
        this.transactionTypeId = transactionTypeId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.userImage = userImage;
    }
}
