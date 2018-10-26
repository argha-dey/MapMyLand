package com.adey.glamorous.utility;

public class Constants {



    /**
     * Prefs Constants
     */
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String REMEMBER_ME_CHECKED = "REMEMBER_ME_CHECKED";
    public static final String USER_ID = "USER_ID";
    public static final String PASSWORD = "PASSWORD";
    public static final String USER_NAME = "USER_NAME";
    public static final String DEPT_ID = "DEPT_ID";
    public static final String DESIG_ID = "DESIG_ID";
    public static final String BAND_ID = "BAND_ID";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String USER_PHOTO = "USER_PHOTO";
    public static final String CHECKIN_STATUS = "CHECKIN_STATUS";
    public static final String CHECKIN_TYPE = "CHECKIN_TYPE";


    /**
     * Request Constants
     */

    public static final int MULTIPLE_PERMISSIONS_REQ = 10000;
    public static final int AUTO_ATTENDANCE_ALERT_REQ = 10001;
    public static final int ATTENDANCE_AFTER_DAY_END_ALERT_REQ = 10002;


    /**
     * Common Constants
     */
    public static final long ONE_HOUR_INTERVAL = 60 * 60 * 1000; // 60*60*1000 ;
    public static final String IS_WORKING = "is_working";


    /**
     * Common Data Base Constants
     */
    public static final String DATABASE_NAME = "CYBER_ENGINE.db";
    public static final String AUTO_ATTENDANCE_TABLE = "AUTO_ATTENDANCE_TABLE";
    public static final String DB_ID = "ID";
    public static final String DB_USER_ID = "USER_ID";
    public static final String DB_DATE_TIME = "DATE_TIME";
    public static final String DB_MOBILE_STATUS = "MOBILE_STATUS";
    public static final String DB_UPDATE_FLAG = "UPDATE_FLAG";
    public static final String DB_FUSE_LAT = "FUSE_LAT";
    public static final String DB_FUSE_LON = "FUSE_LON";


    /**
     * Notification ID Constants
     */
    public static final int MANUAL_ATTENDANCE_AFTER_DAY_END_CUSTOM = 1000;
    public static final int MANUAL_ATTENDANCE_AFTER_DAY_END = 1001;
    public static final int AUTO_ATTENDANCE = 1002;


    /**
     * Navigation Menu Constants
     */
    public static final String HOME = "Home";
    public static final int HOME_ITEM_ID = 0;
    public static final String ATTENDANCE_HISTORY = "Attendance History";
    public static final int ATTENDANCE_HISTORY_ITEM_ID = 1;
    public static final String PO_ENTRY = "PO Entry";
    public static final int PO_ENTRY_ITEM_ID = 2;
    public static final String SETTINGS = "Settings";
    public static final int SETTINGS_ITEM_ID = 2;
    public static final String LOGOUT = "Logout";
    public static final int LOGOUT_ITEM_ID = 3;


    /**
     * Dropdown related constants
     */
    public static final String SELECT_PROJECT = "Select project";
    public static final String SELECT_SUB_PROJECT = "Select sub project";
    public static final String PROJECT_NOT_AVAILABLE = "Project not available";
    public static final String SUB_PROJECT_NOT_AVAILABLE = "Sub project not available";

}
