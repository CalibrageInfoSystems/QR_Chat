package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserinfo {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }


public class Result {

    @SerializedName("isLockedOut")
    @Expose
    private Boolean isLockedOut;
    @SerializedName("emailConfirmed")
    @Expose
    private Boolean emailConfirmed;
    @SerializedName("roles")
    @Expose
    private Object roles;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileLocation")
    @Expose
    private String fileLocation;
    @SerializedName("fileExtension")
    @Expose
    private String fileExtension;
    @SerializedName("fileUrl")
    @Expose
    private String fileUrl;
    @SerializedName("qrContactImage")
    @Expose
    private Object qrContactImage;
    @SerializedName("qrUserImageImage")
    @Expose
    private Object qrUserImageImage;
    @SerializedName("walletId")
    @Expose
    private String walletId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("jobTitle")
    @Expose
    private Object jobTitle;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("configuration")
    @Expose
    private Object configuration;
    @SerializedName("isEnabled")
    @Expose
    private Boolean isEnabled;

    public Boolean getIsLockedOut() {
        return isLockedOut;
    }

    public void setIsLockedOut(Boolean isLockedOut) {
        this.isLockedOut = isLockedOut;
    }

    public Boolean getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public Object getRoles() {
        return roles;
    }

    public void setRoles(Object roles) {
        this.roles = roles;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Object getQrContactImage() {
        return qrContactImage;
    }

    public void setQrContactImage(Object qrContactImage) {
        this.qrContactImage = qrContactImage;
    }

    public Object getQrUserImageImage() {
        return qrUserImageImage;
    }

    public void setQrUserImageImage(Object qrUserImageImage) {
        this.qrUserImageImage = qrUserImageImage;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(Object jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Object configuration) {
        this.configuration = configuration;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

}}