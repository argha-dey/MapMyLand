package com.cyberswift.cyberengine.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SubProjectList implements Parcelable {

    private String edit;
    private String projectId;
    private String subProject;
    private String subProjectCode;
    private String subProjectName;
    private String subProjectId;
    private String userId;

    public SubProjectList() {
    }

    public SubProjectList(String edit, String projectId, String subProject, String subProjectCode, String subProjectName, String subProjectId, String userId) {
        this.edit = edit;
        this.projectId = projectId;
        this.subProject = subProject;
        this.subProjectCode = subProjectCode;
        this.subProjectName = subProjectName;
        this.subProjectId = subProjectId;
        this.userId = userId;
    }

    protected SubProjectList(Parcel in) {
        edit = in.readString();
        projectId = in.readString();
        subProject = in.readString();
        subProjectCode = in.readString();
        subProjectName = in.readString();
        subProjectId = in.readString();
        userId = in.readString();
    }

    public static final Creator<SubProjectList> CREATOR = new Creator<SubProjectList>() {
        @Override
        public SubProjectList createFromParcel(Parcel in) {
            return new SubProjectList(in);
        }

        @Override
        public SubProjectList[] newArray(int size) {
            return new SubProjectList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(edit);
        parcel.writeString(projectId);
        parcel.writeString(subProject);
        parcel.writeString(subProjectCode);
        parcel.writeString(subProjectName);
        parcel.writeString(subProjectId);
        parcel.writeString(userId);
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSubProject() {
        return subProject;
    }

    public void setSubProject(String subProject) {
        this.subProject = subProject;
    }

    public String getSubProjectCode() {
        return subProjectCode;
    }

    public void setSubProjectCode(String subProjectCode) {
        this.subProjectCode = subProjectCode;
    }

    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Creator<SubProjectList> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "SubProjectList{" +
                "edit='" + edit + '\'' +
                ", projectId='" + projectId + '\'' +
                ", subProject='" + subProject + '\'' +
                ", subProjectCode='" + subProjectCode + '\'' +
                ", subProjectName='" + subProjectName + '\'' +
                ", subProjectId='" + subProjectId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

}
