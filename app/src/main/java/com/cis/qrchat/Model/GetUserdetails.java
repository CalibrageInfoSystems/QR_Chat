package com.cis.qrchat.Model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserdetails {

    @SerializedName("isLockedOut")
    @Expose
    private Boolean isLockedOut;
    @SerializedName("emailConfirmed")
    @Expose
    private Boolean emailConfirmed;
    @SerializedName("roles")
    @Expose
    private List<String> roles = null;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileLocation")
    @Expose
    private String fileLocation;
    @SerializedName("fileExtension")
    @Expose
    private String fileExtension;
    @SerializedName("fileUrl")
    @Expose
    private String fileUrl;
    @SerializedName("qrContactImage")
    @Expose
    private String qrContactImage;
    @SerializedName("qrUserImageImage")
    @Expose
    private String qrUserImageImage;
    @SerializedName("walletId")
    @Expose
    private String walletId;
    @SerializedName("regionId")
    @Expose
    private Integer regionId;
    @SerializedName("genderTypeId")
    @Expose
    private Integer genderTypeId;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("email")
    @Expose
    private String email;



    @SerializedName("status")
    @Expose
    private String status ;
    @SerializedName("jobTitle")
    @Expose
    private Object jobTitle;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("configuration")
    @Expose
    private Object configuration;
    @SerializedName("isEnabled")
    @Expose
    private Boolean isEnabled;

    public Boolean getIsLockedOut() {
        return isLockedOut;
    }

    public void setIsLockedOut(Boolean isLockedOut) {
        this.isLockedOut = isLockedOut;
    }

    public Boolean getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getQrContactImage() {
        return qrContactImage;
    }

    public void setQrContactImage(String qrContactImage) {
        this.qrContactImage = qrContactImage;
    }

    public String getQrUserImageImage() {
        return qrUserImageImage;
    }

    public void setQrUserImageImage(String qrUserImageImage) {
        this.qrUserImageImage = qrUserImageImage;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getGenderTypeId() {
        return genderTypeId;
    }

    public void setGenderTypeId(Integer genderTypeId) {
        this.genderTypeId = genderTypeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(Object jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Object configuration) {
        this.configuration = configuration;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

}