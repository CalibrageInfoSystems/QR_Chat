package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profileresponse {

    @SerializedName("isLockedOut")
    @Expose
    private Boolean isLockedOut;
    @SerializedName("emailConfirmed")
    @Expose
    private Boolean emailConfirmed;
    @SerializedName("roles")
    @Expose
    private List<String> roles = null;
    @SerializedName("fileName")
    @Expose
    private Object fileName;
    @SerializedName("fileLocation")
    @Expose
    private Object fileLocation;
    @SerializedName("fileExtension")
    @Expose
    private Object fileExtension;
    @SerializedName("fileUrl")
    @Expose
    private String fileUrl;
    @SerializedName("qrContactImage")
    @Expose
    private String qrContactImage;
    @SerializedName("qrUserImageImage")
    @Expose
    private String qrUserImageImage;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Object getFileName() {
        return fileName;
    }

    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    public Object getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(Object fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Object getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(Object fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getQrContactImage() {
        return qrContactImage;
    }

    public void setQrContactImage(String qrContactImage) {
        this.qrContactImage = qrContactImage;
    }

    public String getQrUserImageImage() {
        return qrUserImageImage;
    }

    public void setQrUserImageImage(String qrUserImageImage) {
        this.qrUserImageImage = qrUserImageImage;
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

}
