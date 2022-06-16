package com.anaamalais.salescrm.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class TestdriveCompleted {

    @SerializedName("BOOKING_FOLLOWUP")
    @Expose
    private String bookingFollowup;
    @SerializedName("FOLLOW_UP_REMARK")
    @Expose
    private String followUpRemark;
    @SerializedName("DRIVING_LICENCE")
    @Expose
    private MultipartBody.Part drivingLicence;
    @SerializedName("FOLLOW_UP_DATE")
    @Expose
    private String followUpDate;
    @SerializedName("FOLLOW_UP_TYPE")
    @Expose
    private String followUpType;
    @SerializedName("OVERALL_CONDITION_OF_VEHICLE")
    @Expose
    private String overallConditionOfVehicle;
    @SerializedName("FOLLOW_UP_TIME")
    @Expose
    private String followUpTime;
    @SerializedName("TEST_DRIVE_ID")
    @Expose
    private String testDriveId;
    @SerializedName("SALES_CONSULTANT_KNOWLEDGE_OF_THE_PRODUCT")
    @Expose
    private String salesConsultantKnowledgeOfTheProduct;
    @SerializedName("OVERALL_SALES_CONSULTANT_KNOWLEDGE_EXPERIENCE")
    @Expose
    private String overallSalesConsultantKnowledgeExperience;
    @SerializedName("DRIVING_LICENCE_NUMBER")
    @Expose
    private String drivingLicenceNumber;
    @SerializedName("OVERALL_TD_EXPERIANCE")
    @Expose
    private String overallTdExperiance;

    public String getBookingFollowup() {
        return bookingFollowup;
    }

    public void setBookingFollowup(String bookingFollowup) {
        this.bookingFollowup = bookingFollowup;
    }

    public String getFollowUpRemark() {
        return followUpRemark;
    }

    public void setFollowUpRemark(String followUpRemark) {
        this.followUpRemark = followUpRemark;
    }

    public MultipartBody.Part getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(MultipartBody.Part drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getFollowUpType() {
        return followUpType;
    }

    public void setFollowUpType(String followUpType) {
        this.followUpType = followUpType;
    }

    public String getOverallConditionOfVehicle() {
        return overallConditionOfVehicle;
    }

    public void setOverallConditionOfVehicle(String overallConditionOfVehicle) {
        this.overallConditionOfVehicle = overallConditionOfVehicle;
    }

    public String getFollowUpTime() {
        return followUpTime;
    }

    public void setFollowUpTime(String followUpTime) {
        this.followUpTime = followUpTime;
    }

    public String getTestDriveId() {
        return testDriveId;
    }

    public void setTestDriveId(String testDriveId) {
        this.testDriveId = testDriveId;
    }

    public String getSalesConsultantKnowledgeOfTheProduct() {
        return salesConsultantKnowledgeOfTheProduct;
    }

    public void setSalesConsultantKnowledgeOfTheProduct(String salesConsultantKnowledgeOfTheProduct) {
        this.salesConsultantKnowledgeOfTheProduct = salesConsultantKnowledgeOfTheProduct;
    }

    public String getOverallSalesConsultantKnowledgeExperience() {
        return overallSalesConsultantKnowledgeExperience;
    }

    public void setOverallSalesConsultantKnowledgeExperience(String overallSalesConsultantKnowledgeExperience) {
        this.overallSalesConsultantKnowledgeExperience = overallSalesConsultantKnowledgeExperience;
    }

    public String getDrivingLicenceNumber() {
        return drivingLicenceNumber;
    }

    public void setDrivingLicenceNumber(String drivingLicenceNumber) {
        this.drivingLicenceNumber = drivingLicenceNumber;
    }

    public String getOverallTdExperiance() {
        return overallTdExperiance;
    }

    public void setOverallTdExperiance(String overallTdExperiance) {
        this.overallTdExperiance = overallTdExperiance;
    }

}
