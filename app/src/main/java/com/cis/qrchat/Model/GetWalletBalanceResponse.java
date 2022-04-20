package com.cis.qrchat.Model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class GetWalletBalanceResponse {

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

            @SerializedName("walletId")
            @Expose
            private String walletId;
            @SerializedName("walletBalance")
            @Expose
            private Integer walletBalance;

            public String getWalletId() {
                return walletId;
            }

            public void setWalletId(String walletId) {
                this.walletId = walletId;
            }

            public Integer getWalletBalance() {
                return walletBalance;
            }

            public void setWalletBalance(Integer walletBalance) {
                this.walletBalance = walletBalance;
            }

        }
}
