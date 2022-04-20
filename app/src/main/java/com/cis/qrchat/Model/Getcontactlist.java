package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Getcontactlist {

    @SerializedName("listResult")
    @Expose
    private List<ListResult> listResult = null;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("affectedRecords")
    @Expose
    private Integer affectedRecords;
    @SerializedName("endUserMessage")
    @Expose
    private String endUserMessage;
    @SerializedName("validationErrors")
    @Expose
    private List<Object> validationErrors = null;
    @SerializedName("exception")
    @Expose
    private Object exception;

    public List<ListResult> getListResult() {
        return listResult;
    }

    public void setListResult(List<ListResult> listResult) {
        this.listResult = listResult;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Integer getAffectedRecords() {
        return affectedRecords;
    }

    public void setAffectedRecords(Integer affectedRecords) {
        this.affectedRecords = affectedRecords;
    }

    public String getEndUserMessage() {
        return endUserMessage;
    }

    public void setEndUserMessage(String endUserMessage) {
        this.endUserMessage = endUserMessage;
    }

    public List<Object> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<Object> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }



public class ListResult {

    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("contactId")
    @Expose
    private String contactId;
    @SerializedName("walletId")
    @Expose
    private String walletId;
    @SerializedName("contactName")
    @Expose
    private String contactName;
    @SerializedName("nickName")
    @Expose
    private String nickName;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("contactQrChatId")
    @Expose
    private String contactQrChatId;
    @SerializedName("fileName")
    @Expose
    private Object fileName;
    @SerializedName("fileExtension")
    @Expose
    private Object fileExtension;
    @SerializedName("fileLocation")
    @Expose
    private Object fileLocation;
    @SerializedName("userImage")
    @Expose
    private String userImage;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactQrChatId() {
        return contactQrChatId;
    }

    public void setContactQrChatId(String contactQrChatId) {
        this.contactQrChatId = contactQrChatId;
    }

    public Object getFileName() {
        return fileName;
    }

    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    public Object getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(Object fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Object getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(Object fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }
}}