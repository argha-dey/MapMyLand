package com.cyberswift.cyberengine.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProjectLogDetails implements Parcelable {

    private String projectId;
    private String projectName;
    private ArrayList<SubProjectLogDetails> logSubDetails;

    public ProjectLogDetails() {
    }

    protected ProjectLogDetails(Parcel in) {
        projectId = in.readString();
        projectName = in.readString();
    }

    public static final Creator<ProjectLogDetails> CREATOR = new Creator<ProjectLogDetails>() {
        @Override
        public ProjectLogDetails createFromParcel(Parcel in) {
            return new ProjectLogDetails(in);
        }

        @Override
        public ProjectLogDetails[] newArray(int size) {
            return new ProjectLogDetails[size];
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

    public ArrayList<SubProjectLogDetails> getLogSubDetails() {
        return logSubDetails;
    }

    public void setLogSubDetails(ArrayList<SubProjectLogDetails> logSubDetails) {
        this.logSubDetails = logSubDetails;
    }

    public static Creator<ProjectLogDetails> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "ProjectLogDetails{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", logSubDetails=" + logSubDetails +
                '}';
    }

}
