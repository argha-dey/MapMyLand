package com.cyberswift.cyberengine.models;

import java.io.Serializable;

/**
 * Created by User-129-pc on 25-07-2018.
 */

public class AttendanceUserClass implements Serializable {

    private int local_id;
    private int user_id;
    private String lat;
    private String lon;
    private String date_time;
    private int update_flag;
    private String mobileStatus;


    public AttendanceUserClass(int local_id, int user_id, String lat, String lon, String date_time, int update_flag, String mobileStatus) {
        this.user_id = user_id;
        this.local_id = local_id;
        this.lat = lat;
        this.lon = lon;
        this.date_time = date_time;
        this.update_flag = update_flag;
        this.mobileStatus = mobileStatus;
    }

    public AttendanceUserClass(int user_id, String lat, String lon, String date_time, int update_flag, String mobileStatus) {
        this.user_id = user_id;
        this.lat = lat;
        this.lon = lon;
        this.date_time = date_time;
        this.update_flag = update_flag;
        this.mobileStatus = mobileStatus;
    }

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    public int getUpdate_flag() {
        return update_flag;
    }

    public void setUpdate_flag(int update_flag) {
        this.update_flag = update_flag;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
