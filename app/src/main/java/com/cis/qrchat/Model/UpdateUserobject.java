package com.cis.qrchat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserobject {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("regionId")
    @Expose
    private Integer regionId;
    @SerializedName("fileName")
    @Expose
    private Object fileName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("fileLocation")
    @Expose
    private Object fileLocation;
    @SerializedName("fileExtension")
    @Expose
    private Object fileExtension;
    @SerializedName("genderTypeId")
    @Expose
    private Integer genderTypeId;
    @SerializedName("location")
    @Expose
    private String location;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Object getFileName() {
        return fileName;
    }

    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(Object fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Object getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(Object fileExtension) {
        this.fileExtension = fileExtension;
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
    }}