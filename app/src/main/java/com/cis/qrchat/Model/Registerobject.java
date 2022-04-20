package com.cis.qrchat.Model;

    import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class Registerobject {

        @SerializedName("id")
        @Expose
        private Object id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("regionId")
        @Expose
        private Integer regionId;
        @SerializedName("fileName")
        @Expose
        private String fileName;
        @SerializedName("fileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("fileExtension")
        @Expose
        private String fileExtension;
        @SerializedName("genderTypeId")
        @Expose
        private Integer genderTypeId;
        @SerializedName("location")
        @Expose
        private String location;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
}
