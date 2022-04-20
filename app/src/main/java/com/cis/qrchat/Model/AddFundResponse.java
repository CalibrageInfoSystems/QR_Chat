package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFundResponse {

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

    public class AppGroupFundXrefFundId {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("fundId")
        @Expose
        private Integer fundId;
        @SerializedName("groupId")
        @Expose
        private String groupId;
        @SerializedName("participantId")
        @Expose
        private String participantId;
        @SerializedName("amountToPay")
        @Expose
        private Double amountToPay;
        @SerializedName("balanceToPay")
        @Expose
        private Double balanceToPay;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getFundId() {
            return fundId;
        }

        public void setFundId(Integer fundId) {
            this.fundId = fundId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getParticipantId() {
            return participantId;
        }

        public void setParticipantId(String participantId) {
            this.participantId = participantId;
        }

        public Double getAmountToPay() {
            return amountToPay;
        }

        public void setAmountToPay(Double amountToPay) {
            this.amountToPay = amountToPay;
        }

        public Double getBalanceToPay() {
            return balanceToPay;
        }

        public void setBalanceToPay(Double balanceToPay) {
            this.balanceToPay = balanceToPay;
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

    }

    public class AppGroupTransactionFundId {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("fundId")
        @Expose
        private Integer fundId;
        @SerializedName("groupId")
        @Expose
        private String groupId;
        @SerializedName("participantId")
        @Expose
        private String participantId;
        @SerializedName("amountToPay")
        @Expose
        private String amountToPay;
        @SerializedName("remarks")
        @Expose
        private Object remarks;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getFundId() {
            return fundId;
        }

        public void setFundId(Integer fundId) {
            this.fundId = fundId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getParticipantId() {
            return participantId;
        }

        public void setParticipantId(String participantId) {
            this.participantId = participantId;
        }

        public String getAmountToPay() {
            return amountToPay;
        }

        public void setAmountToPay(String amountToPay) {
            this.amountToPay = amountToPay;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
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

    }

    public class Result {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("groupId")
        @Expose
        private String groupId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("participantId")
        @Expose
        private String participantId;
        @SerializedName("totalAmount")
        @Expose
        private Integer totalAmount;
        @SerializedName("balAmount")
        @Expose
        private Double balAmount;
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
        @SerializedName("app_GroupFundXref_FundId")
        @Expose
        private List<AppGroupFundXrefFundId> appGroupFundXrefFundId = null;
        @SerializedName("app_GroupTransaction_FundId")
        @Expose
        private List<AppGroupTransactionFundId> appGroupTransactionFundId = null;
        @SerializedName("app_Transaction_FundId")
        @Expose
        private Object appTransactionFundId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParticipantId() {
            return participantId;
        }

        public void setParticipantId(String participantId) {
            this.participantId = participantId;
        }

        public Integer getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Double getBalAmount() {
            return balAmount;
        }

        public void setBalAmount(Double balAmount) {
            this.balAmount = balAmount;
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

        public List<AppGroupFundXrefFundId> getAppGroupFundXrefFundId() {
            return appGroupFundXrefFundId;
        }

        public void setAppGroupFundXrefFundId(List<AppGroupFundXrefFundId> appGroupFundXrefFundId) {
            this.appGroupFundXrefFundId = appGroupFundXrefFundId;
        }

        public List<AppGroupTransactionFundId> getAppGroupTransactionFundId() {
            return appGroupTransactionFundId;
        }

        public void setAppGroupTransactionFundId(List<AppGroupTransactionFundId> appGroupTransactionFundId) {
            this.appGroupTransactionFundId = appGroupTransactionFundId;
        }

        public Object getAppTransactionFundId() {
            return appTransactionFundId;
        }

        public void setAppTransactionFundId(Object appTransactionFundId) {
            this.appTransactionFundId = appTransactionFundId;
        }

    }

}


