package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetGroupMembers {

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

    public static class ListResult {

        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("nickName")
        @Expose
        private String nickName;
        @SerializedName("phoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("qrChatId")
        @Expose
        private String qrChatId;
        @SerializedName("fileName")
        @Expose
        private String fileName;
        @SerializedName("fileExtension")
        @Expose
        private String fileExtension;
        @SerializedName("fileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("userImage")
        @Expose
        private String userImage;

        @SerializedName("equalamount")
        @Expose
        private Double equalamount;

        @SerializedName("issSelected")
        @Expose
        private Boolean isSelected;



        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getQrChatId() {
            return qrChatId;
        }

        public void setQrChatId(String qrChatId) {
            this.qrChatId = qrChatId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public void setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public Double getEqualamount() {
            return equalamount;
        }

        public void setEqualamount(Double equalamount) {
            this.equalamount = equalamount;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }


        public ListResult(String userId, String name, String nickName, String phoneNumber, String qrChatId, String fileName, String fileExtension, String fileLocation, String userImage, Double equalamount, Boolean isSelected) {
            this.userId = userId;
            this.name = name;
            this.nickName = nickName;
            this.phoneNumber = phoneNumber;
            this.qrChatId = qrChatId;
            this.fileName = fileName;
            this.fileExtension = fileExtension;
            this.fileLocation = fileLocation;
            this.userImage = userImage;
            this.equalamount = equalamount;
            this.isSelected = isSelected;
        }
    }
}
