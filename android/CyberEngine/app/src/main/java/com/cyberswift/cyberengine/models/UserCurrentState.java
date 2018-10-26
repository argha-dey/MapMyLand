package com.cyberswift.cyberengine.models;

import java.io.Serializable;

/**
 * Created by User-129-pc on 17-04-2018.
 */

public class UserCurrentState implements Serializable {

    private String projectName;
    private String projectState;
    private String hasAlarmStarted;
    private boolean projectStateFlag = false;
    private String projectId;
    private String projectUserId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    public boolean getProjectStateFlag() {
        return projectStateFlag;
    }

    public void setProjectStateFlag(boolean projectStateFlag) {
        this.projectStateFlag = projectStateFlag;
    }

    public String hasAlarmStarted() {
        return hasAlarmStarted;
    }

    public void setAlarmStarted(String hasAlarmStarted) {
        this.hasAlarmStarted = hasAlarmStarted;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectUserId() {
        return projectUserId;
    }

    public void setProjectUserId(String projectUserId) {
        this.projectUserId = projectUserId;
    }

    public void UserCurrentStateClear() {
        setProjectName("");
        setProjectState("");
        setProjectId("");
        setProjectUserId("");
        projectStateFlag = false;
    }
}
