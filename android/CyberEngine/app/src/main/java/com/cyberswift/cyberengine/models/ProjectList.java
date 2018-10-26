package com.cyberswift.cyberengine.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProjectList implements Parcelable {

    private String projectId;
    private String projectName;
    private String projectType;
    private String statusEnd;
    private ArrayList<SubProjectList> subProjectList;

    public ProjectList() {
    }

    public ProjectList(String projectId, String projectName, String projectType, String statusEnd, ArrayList<SubProjectList> subProjectList) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectType = projectType;
        this.statusEnd = statusEnd;
        this.subProjectList = subProjectList;
    }

    protected ProjectList(Parcel in) {
        projectId = in.readString();
        projectName = in.readString();
        projectType = in.readString();
        statusEnd = in.readString();
        subProjectList = in.createTypedArrayList(SubProjectList.CREATOR);
    }

    public static final Creator<ProjectList> CREATOR = new Creator<ProjectList>() {
        @Override
        public ProjectList createFromParcel(Parcel in) {
            return new ProjectList(in);
        }

        @Override
        public ProjectList[] newArray(int size) {
            return new ProjectList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(projectId);
        parcel.writeString(projectName);
        parcel.writeString(projectType);
        parcel.writeString(statusEnd);
        parcel.writeTypedList(subProjectList);
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getStatusEnd() {
        return statusEnd;
    }

    public void setStatusEnd(String statusEnd) {
        this.statusEnd = statusEnd;
    }

    public ArrayList<SubProjectList> getSubProjectList() {
        return subProjectList;
    }

    public void setSubProjectList(ArrayList<SubProjectList> subProjectList) {
        this.subProjectList = subProjectList;
    }

    public static Creator<ProjectList> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "ProjectList{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectType='" + projectType + '\'' +
                ", statusEnd='" + statusEnd + '\'' +
                ", subProjectList=" + subProjectList +
                '}';
    }

}
