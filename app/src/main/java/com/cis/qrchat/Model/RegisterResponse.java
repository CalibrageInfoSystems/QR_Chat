package com.cis.qrchat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("message")
    @Expose
    private String message;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("description")
    @Expose
    private String description;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}