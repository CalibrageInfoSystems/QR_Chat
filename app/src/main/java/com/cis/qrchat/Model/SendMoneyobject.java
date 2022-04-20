package com.cis.qrchat.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



    public class SendMoneyobject {

        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("userWalletHistory")
        @Expose
        private UserWalletHistory userWalletHistory;
        @SerializedName("recieverUserName")
        @Expose
        private String recieverUserName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public UserWalletHistory getUserWalletHistory() {
            return userWalletHistory;
        }

        public void setUserWalletHistory(UserWalletHistory userWalletHistory) {
            this.userWalletHistory = userWalletHistory;
        }

        public String getRecieverUserName() {
            return recieverUserName;
        }

        public void setRecieverUserName(String recieverUserName) {
            this.recieverUserName = recieverUserName;
        }


        public SendMoneyobject(UserWalletHistory requestHeader) {
            this.userWalletHistory = requestHeader;

        }

    public static class UserWalletHistory {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("walletId")
        @Expose
        private String walletId;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("cardId")
        @Expose
        private String cardId;
        @SerializedName("transactionTypeId")
        @Expose
        private Integer transactionTypeId;
        @SerializedName("reasonTypeId")
        @Expose
        private Integer reasonTypeId;
        @SerializedName("groupId")
        @Expose
        private String groupId;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("isMoneyRequest")
        @Expose
        private Boolean isMoneyRequest;
        @SerializedName("requestId")
        @Expose
        private String requestId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

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

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
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

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Boolean getIsMoneyRequest() {
            return isMoneyRequest;
        }

        public void setIsMoneyRequest(Boolean isMoneyRequest) {
            this.isMoneyRequest = isMoneyRequest;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }}}