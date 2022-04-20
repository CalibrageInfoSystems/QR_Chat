package com.cis.qrchat.Model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class DeleteCard {


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
        @SerializedName("cardTypeId")
        @Expose
        private Integer cardTypeId;
        @SerializedName("bankName")
        @Expose
        private String bankName;
        @SerializedName("cardName")
        @Expose
        private String cardName;
        @SerializedName("cardNumber")
        @Expose
        private String cardNumber;
        @SerializedName("expiredOn")
        @Expose
        private String expiredOn;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("isDefault")
        @Expose
        private Boolean isDefault;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;

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

        public Integer getCardTypeId() {
            return cardTypeId;
        }

        public void setCardTypeId(Integer cardTypeId) {
            this.cardTypeId = cardTypeId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getExpiredOn() {
            return expiredOn;
        }

        public void setExpiredOn(String expiredOn) {
            this.expiredOn = expiredOn;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Boolean getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(Boolean isDefault) {
            this.isDefault = isDefault;
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
    }}