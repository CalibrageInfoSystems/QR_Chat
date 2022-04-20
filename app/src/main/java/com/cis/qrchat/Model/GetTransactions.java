package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransactions {

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

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("clientId")
    @Expose
    private Object clientId;
    @SerializedName("transactionType")
    @Expose
    private String transactionType;
    @SerializedName("totalAmount")
    @Expose
    private Integer totalAmount;
    @SerializedName("paymentAmount")
    @Expose
    private Integer paymentAmount;
    @SerializedName("serviceFeeAmount")
    @Expose
    private Integer serviceFeeAmount;
    @SerializedName("currency")
    @Expose
    private Object currency;
    @SerializedName("clientRef")
    @Expose
    private Object clientRef;
    @SerializedName("comment")
    @Expose
    private Object comment;
    @SerializedName("tokenize")
    @Expose
    private Object tokenize;
    @SerializedName("tokenReference")
    @Expose
    private Object tokenReference;
    @SerializedName("reqid")
    @Expose
    private Object reqid;
    @SerializedName("cardId")
    @Expose
    private Object cardId;
    @SerializedName("secureIdSupplied")
    @Expose
    private Object secureIdSupplied;
    @SerializedName("txnReference")
    @Expose
    private Object txnReference;
    @SerializedName("responseCode")
    @Expose
    private Object responseCode;
    @SerializedName("responseText")
    @Expose
    private Object responseText;
    @SerializedName("token")
    @Expose
    private Object token;
    @SerializedName("tokenized")
    @Expose
    private Object tokenized;
    @SerializedName("tokenResponseText")
    @Expose
    private Object tokenResponseText;
    @SerializedName("tokenAuthTxnReference")
    @Expose
    private Object tokenAuthTxnReference;
    @SerializedName("settlementDate")
    @Expose
    private Object settlementDate;
    @SerializedName("authCode")
    @Expose
    private Object authCode;
    @SerializedName("cvcResponse")
    @Expose
    private Object cvcResponse;
    @SerializedName("requestTime")
    @Expose
    private String requestTime;
    @SerializedName("responseTime")
    @Expose
    private String responseTime;
    @SerializedName("fundId")
    @Expose
    private Object fundId;
    @SerializedName("groupId")
    @Expose
    private Object groupId;

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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Object getClientId() {
        return clientId;
    }

    public void setClientId(Object clientId) {
        this.clientId = clientId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Integer getServiceFeeAmount() {
        return serviceFeeAmount;
    }

    public void setServiceFeeAmount(Integer serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    public Object getCurrency() {
        return currency;
    }

    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    public Object getClientRef() {
        return clientRef;
    }

    public void setClientRef(Object clientRef) {
        this.clientRef = clientRef;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public Object getTokenize() {
        return tokenize;
    }

    public void setTokenize(Object tokenize) {
        this.tokenize = tokenize;
    }

    public Object getTokenReference() {
        return tokenReference;
    }

    public void setTokenReference(Object tokenReference) {
        this.tokenReference = tokenReference;
    }

    public Object getReqid() {
        return reqid;
    }

    public void setReqid(Object reqid) {
        this.reqid = reqid;
    }

    public Object getCardId() {
        return cardId;
    }

    public void setCardId(Object cardId) {
        this.cardId = cardId;
    }

    public Object getSecureIdSupplied() {
        return secureIdSupplied;
    }

    public void setSecureIdSupplied(Object secureIdSupplied) {
        this.secureIdSupplied = secureIdSupplied;
    }

    public Object getTxnReference() {
        return txnReference;
    }

    public void setTxnReference(Object txnReference) {
        this.txnReference = txnReference;
    }

    public Object getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Object responseCode) {
        this.responseCode = responseCode;
    }

    public Object getResponseText() {
        return responseText;
    }

    public void setResponseText(Object responseText) {
        this.responseText = responseText;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public Object getTokenized() {
        return tokenized;
    }

    public void setTokenized(Object tokenized) {
        this.tokenized = tokenized;
    }

    public Object getTokenResponseText() {
        return tokenResponseText;
    }

    public void setTokenResponseText(Object tokenResponseText) {
        this.tokenResponseText = tokenResponseText;
    }

    public Object getTokenAuthTxnReference() {
        return tokenAuthTxnReference;
    }

    public void setTokenAuthTxnReference(Object tokenAuthTxnReference) {
        this.tokenAuthTxnReference = tokenAuthTxnReference;
    }

    public Object getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Object settlementDate) {
        this.settlementDate = settlementDate;
    }

    public Object getAuthCode() {
        return authCode;
    }

    public void setAuthCode(Object authCode) {
        this.authCode = authCode;
    }

    public Object getCvcResponse() {
        return cvcResponse;
    }

    public void setCvcResponse(Object cvcResponse) {
        this.cvcResponse = cvcResponse;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Object getFundId() {
        return fundId;
    }

    public void setFundId(Object fundId) {
        this.fundId = fundId;
    }

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }
}}