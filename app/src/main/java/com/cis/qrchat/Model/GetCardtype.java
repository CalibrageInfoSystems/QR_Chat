package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GetCardtype {
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
        @SerializedName("classTypeId")
        @Expose
        private Integer classTypeId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("sortOrder")
        @Expose
        private Integer sortOrder;
        @SerializedName("tableName")
        @Expose
        private String tableName;
        @SerializedName("columnName")
        @Expose
        private String columnName;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("app_LookUp_LookUpTypeId")
        @Expose
        private Object appLookUpLookUpTypeId;
        @SerializedName("app_User_GenderTypeId")
        @Expose
        private Object appUserGenderTypeId;
        @SerializedName("app_UserCard_CardTypeId")
        @Expose
        private Object appUserCardCardTypeId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getClassTypeId() {
            return classTypeId;
        }

        public void setClassTypeId(Integer classTypeId) {
            this.classTypeId = classTypeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Object getAppLookUpLookUpTypeId() {
            return appLookUpLookUpTypeId;
        }

        public void setAppLookUpLookUpTypeId(Object appLookUpLookUpTypeId) {
            this.appLookUpLookUpTypeId = appLookUpLookUpTypeId;
        }

        public Object getAppUserGenderTypeId() {
            return appUserGenderTypeId;
        }

        public void setAppUserGenderTypeId(Object appUserGenderTypeId) {
            this.appUserGenderTypeId = appUserGenderTypeId;
        }

        public Object getAppUserCardCardTypeId() {
            return appUserCardCardTypeId;
        }

        public void setAppUserCardCardTypeId(Object appUserCardCardTypeId) {
            this.appUserCardCardTypeId = appUserCardCardTypeId;
        }

}}
