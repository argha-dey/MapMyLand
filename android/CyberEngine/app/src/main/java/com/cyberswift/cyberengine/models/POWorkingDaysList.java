package com.cyberswift.cyberengine.models;

public class POWorkingDaysList {

    private String createdById, day, isAbsent, logDate, billedHours, totalHours;
    private int weekNo;

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIsAbsent() {
        return isAbsent;
    }

    public void setIsAbsent(String isAbsent) {
        this.isAbsent = isAbsent;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getBilledHours() {
        return billedHours;
    }

    public void setBilledHours(String billedHours) {
        this.billedHours = billedHours;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    @Override
    public String toString() {
        return "POWorkingDaysList{" +
                "createdById='" + createdById + '\'' +
                ", day='" + day + '\'' +
                ", isAbsent='" + isAbsent + '\'' +
                ", logDate='" + logDate + '\'' +
                ", billedHours='" + billedHours + '\'' +
                ", totalHours='" + totalHours + '\'' +
                ", weekNo=" + weekNo +
                '}';
    }

}
