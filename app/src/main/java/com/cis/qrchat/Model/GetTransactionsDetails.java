package com.cis.qrchat.Model;

    import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class GetTransactionsDetails {

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

        @SerializedName("walletId")
        @Expose
        private String walletId;
        @SerializedName("amount")
        @Expose
        private Double amount;
        @SerializedName("transactionTypeId")
        @Expose
        private Integer transactionTypeId;
        @SerializedName("transactionType")
        @Expose
        private String transactionType;
        @SerializedName("transactionId")
        @Expose
        private String transactionId;
        @SerializedName("created")
        @Expose
        private String created;

        public String getWalletId() {
            return walletId;
        }

        public void setWalletId(String walletId) {
            this.walletId = walletId;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Integer getTransactionTypeId() {
            return transactionTypeId;
        }

        public void setTransactionTypeId(Integer transactionTypeId) {
            this.transactionTypeId = transactionTypeId;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

    }}
