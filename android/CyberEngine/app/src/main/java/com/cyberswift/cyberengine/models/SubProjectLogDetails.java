package com.cyberswift.cyberengine.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SubProjectLogDetails implements Parcelable {

    private String subProjectId;
    private String subProjectName;
    private String description;
    private String totalLoggedHours;
    private String billableHours;
    private String approvedHours;
    private String rejectedHours;
    private String createdById;
    private String empDeptId;
    private String id;
    private String projectDeptId;
    private String projectId;
    private String projectMemberId;
    private String projectName;
    private String projectType;

    public SubProjectLogDetails() {
    }


    protected SubProjectLogDetails(Parcel in) {
        subProjectId = in.readString();
        subProjectName = in.readString();
        description = in.readString();
        totalLoggedHours = in.readString();
        billableHours = in.readString();
        approvedHours = in.readString();
        rejectedHours = in.readString();
        createdById = in.readString();
        empDeptId = in.readString();
        id = in.readString();
        projectDeptId = in.readString();
        projectId = in.readString();
        projectMemberId = in.readString();
        projectName = in.readString();
        projectType = in.readString();
    }

    public static final Creator<SubProjectLogDetails> CREATOR = new Creator<SubProjectLogDetails>() {
        @Override
        public SubProjectLogDetails createFromParcel(Parcel in) {
            return new SubProjectLogDetails(in);
        }

        @Override
        public SubProjectLogDetails[] newArray(int size) {
            return new SubProjectLogDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(subProjectId);
        parcel.writeString(subProjectName);
        parcel.writeString(description);
        parcel.writeString(totalLoggedHours);
        parcel.writeString(billableHours);
        parcel.writeString(approvedHours);
        parcel.writeString(rejectedHours);
        parcel.writeString(createdById);
        parcel.writeString(empDeptId);
        parcel.writeString(id);
        parcel.writeString(projectDeptId);
        parcel.writeString(projectId);
        parcel.writeString(projectMemberId);
        parcel.writeString(projectName);
        parcel.writeString(projectType);
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalLoggedHours() {
        return totalLoggedHours;
    }

    public void setTotalLoggedHours(String totalLoggedHours) {
        this.totalLoggedHours = totalLoggedHours;
    }

    public String getBillableHours() {
        return billableHours;
    }

    public void setBillableHours(String billableHours) {
        this.billableHours = billableHours;
    }

    public String getApprovedHours() {
        return approvedHours;
    }

    public void setApprovedHours(String approvedHours) {
        this.approvedHours = approvedHours;
    }

    public String getRejectedHours() {
        return rejectedHours;
    }

    public void setRejectedHours(String rejectedHours) {
        this.rejectedHours = rejectedHours;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getEmpDeptId() {
        return empDeptId;
    }

    public void setEmpDeptId(String empDeptId) {
        this.empDeptId = empDeptId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectDeptId() {
        return projectDeptId;
    }

    public void setProjectDeptId(String projectDeptId) {
        this.projectDeptId = projectDeptId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectMemberId() {
        return projectMemberId;
    }

    public void setProjectMemberId(String projectMemberId) {
        this.projectMemberId = projectMemberId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public static Creator<SubProjectLogDetails> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "SubProjectLogDetails{" +
                "subProjectId='" + subProjectId + '\'' +
                ", subProjectName='" + subProjectName + '\'' +
                ", description='" + description + '\'' +
                ", totalLoggedHours='" + totalLoggedHours + '\'' +
                ", billableHours='" + billableHours + '\'' +
                ", approvedHours='" + approvedHours + '\'' +
                ", rejectedHours='" + rejectedHours + '\'' +
                ", createdById='" + createdById + '\'' +
                ", empDeptId='" + empDeptId + '\'' +
                ", id='" + id + '\'' +
                ", projectDeptId='" + projectDeptId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectMemberId='" + projectMemberId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectType='" + projectType + '\'' +
                '}';
    }

}
