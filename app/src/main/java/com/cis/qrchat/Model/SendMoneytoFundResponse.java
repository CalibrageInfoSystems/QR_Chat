package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMoneytoFundResponse {

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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("fileName")
        @Expose
        private String fileName;
        @SerializedName("fileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("refFileExtension")
        @Expose
        private String refFileExtension;
        @SerializedName("refFileName")
        @Expose
        private String refFileName;
        @SerializedName("refFileLocation")
        @Expose
        private String refFileLocation;
        @SerializedName("fileExtension")
        @Expose
        private String fileExtension;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("app_UserWalletHistory_WalletId")
        @Expose
        private List<AppUserWalletHistoryWalletId> appUserWalletHistoryWalletId = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
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

        public String getRefFileExtension() {
            return refFileExtension;
        }

        public void setRefFileExtension(String refFileExtension) {
            this.refFileExtension = refFileExtension;
        }

        public String getRefFileName() {
            return refFileName;
        }

        public void setRefFileName(String refFileName) {
            this.refFileName = refFileName;
        }

        public String getRefFileLocation() {
            return refFileLocation;
        }

        public void setRefFileLocation(String refFileLocation) {
            this.refFileLocation = refFileLocation;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public void setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public List<AppUserWalletHistoryWalletId> getAppUserWalletHistoryWalletId() {
            return appUserWalletHistoryWalletId;
        }

        public void setAppUserWalletHistoryWalletId(List<AppUserWalletHistoryWalletId> appUserWalletHistoryWalletId) {
            this.appUserWalletHistoryWalletId = appUserWalletHistoryWalletId;
        }

    }
    public class AppUserWalletHistoryWalletId {

        @SerializedName("walletId")
        @Expose
        private String walletId;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("groupId")
        @Expose
        private String groupId;
        @SerializedName("transactionTypeId")
        @Expose
        private Integer transactionTypeId;
        @SerializedName("reasonTypeId")
        @Expose
        private Integer reasonTypeId;
        @SerializedName("transactionId")
        @Expose
        private String transactionId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("createdBy")
        @Expose
        private String createdBy;
        @SerializedName("updatedBy")
        @Expose
        private String updatedBy;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;

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

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
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

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

    }

}
