package com.cis.qrchat.Model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddGroupResponse {

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
    public class AppUserGroupXrefGroupId {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("groupId")
        @Expose
        private String groupId;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

    }

    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("remarks")
        @Expose
        private String remarks;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("statusId")
        @Expose
        private Integer statusId;
        @SerializedName("createdBy")
        @Expose
        private String createdBy;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedBy")
        @Expose
        private String updatedBy;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("app_UserWalletHistory_GroupId")
        @Expose
        private Object appUserWalletHistoryGroupId;
        @SerializedName("app_UserGroupXref_GroupId")
        @Expose
        private List<AppUserGroupXrefGroupId> appUserGroupXrefGroupId = null;
        @SerializedName("app_Funds_GroupId")
        @Expose
        private Object appFundsGroupId;
        @SerializedName("app_GroupFundXref_GroupId")
        @Expose
        private Object appGroupFundXrefGroupId;
        @SerializedName("app_GroupTransaction_GroupId")
        @Expose
        private Object appGroupTransactionGroupId;
        @SerializedName("app_Transaction_GroupId")
        @Expose
        private Object appTransactionGroupId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getStatusId() {
            return statusId;
        }

        public void setStatusId(Integer statusId) {
            this.statusId = statusId;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public Object getAppUserWalletHistoryGroupId() {
            return appUserWalletHistoryGroupId;
        }

        public void setAppUserWalletHistoryGroupId(Object appUserWalletHistoryGroupId) {
            this.appUserWalletHistoryGroupId = appUserWalletHistoryGroupId;
        }

        public List<AppUserGroupXrefGroupId> getAppUserGroupXrefGroupId() {
            return appUserGroupXrefGroupId;
        }

        public void setAppUserGroupXrefGroupId(List<AppUserGroupXrefGroupId> appUserGroupXrefGroupId) {
            this.appUserGroupXrefGroupId = appUserGroupXrefGroupId;
        }

        public Object getAppFundsGroupId() {
            return appFundsGroupId;
        }

        public void setAppFundsGroupId(Object appFundsGroupId) {
            this.appFundsGroupId = appFundsGroupId;
        }

        public Object getAppGroupFundXrefGroupId() {
            return appGroupFundXrefGroupId;
        }

        public void setAppGroupFundXrefGroupId(Object appGroupFundXrefGroupId) {
            this.appGroupFundXrefGroupId = appGroupFundXrefGroupId;
        }

        public Object getAppGroupTransactionGroupId() {
            return appGroupTransactionGroupId;
        }

        public void setAppGroupTransactionGroupId(Object appGroupTransactionGroupId) {
            this.appGroupTransactionGroupId = appGroupTransactionGroupId;
        }

        public Object getAppTransactionGroupId() {
            return appTransactionGroupId;
        }

        public void setAppTransactionGroupId(Object appTransactionGroupId) {
            this.appTransactionGroupId = appTransactionGroupId;
        }

    }

}
