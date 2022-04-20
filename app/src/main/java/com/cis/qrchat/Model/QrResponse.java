package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class QrResponse {

        @SerializedName("result")
        @Expose
        private Result result;
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

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
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


    public class Result {

        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("email")
        @Expose
        private Object email;
        @SerializedName("fullName")
        @Expose
        private String fullName;
        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("walletBallence")
        @Expose
        private Integer walletBallence;
        @SerializedName("walletId")
        @Expose
        private String walletId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public Integer getWalletBallence() {
            return walletBallence;
        }

        public void setWalletBallence(Integer walletBallence) {
            this.walletBallence = walletBallence;
        }

        public String getWalletId() {
            return walletId;
        }

        public void setWalletId(String walletId) {
            this.walletId = walletId;
        }
    }  }