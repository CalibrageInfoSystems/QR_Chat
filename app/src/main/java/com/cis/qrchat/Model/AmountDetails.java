package com.cis.qrchat.Model;

public class AmountDetails {
    String Id;
    double amount;

    public AmountDetails(String id, double amount) {
        Id = id;
        this.amount = amount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
