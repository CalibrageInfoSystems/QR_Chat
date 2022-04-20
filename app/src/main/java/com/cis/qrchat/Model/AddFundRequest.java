package com.cis.qrchat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFundRequest {




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
        private Integer balAmount;
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
        @SerializedName("participants")
        @Expose
        private List<Participant> participants = null;

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

        public Integer getBalAmount() {
            return balAmount;
        }

        public void setBalAmount(Integer balAmount) {
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

        public List<Participant> getParticipants() {
            return participants;
        }

    public AddFundRequest(List<Participant> participants) {
        this.participants = participants;
    }

    public void setParticipants(List<Participant> participants) {
            this.participants = participants;
        }


        public static class Participant {

            @SerializedName("participantId")
            @Expose
            private String participantId;
            @SerializedName("shareAmount")
            @Expose
            private double shareAmount;

            public Participant(String participantId, double shareAmount) {
                this.participantId = participantId;
                this.shareAmount = shareAmount;
            }

            public Participant() {

            }

            public String getParticipantId() {
                return participantId;
            }

            public void setParticipantId(String participantId) {
                this.participantId = participantId;
            }

            public double getShareAmount() {
                return shareAmount;
            }

            public void setShareAmount(double shareAmount) {
                this.shareAmount = shareAmount;
            }
        }

    }