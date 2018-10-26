package com.cyberswift.cyberengine.utility;

public class WebServiceConstants {

    /**
     * Mobile Service Name Tags
     **/
    public static final String URL_GET_MOB_LOGIN = "GetMobLogin";
    public static final String URL_CHECKIN_CHECKOUT = "CommonAttendance";
    public static final String URL_GET_CHECKIN_CHECKOUT_STATUS = "GetCheckInFlag";
    public static final String URL_GET_CHECKIN_CHECKOUT_LIST = "GetCheckInCheckOutList";
    public static final String URL_GET_CURRENT_MONTH_ATTENDANCE_LIST = "GetAttendanceHistory";
    public static final String URL_SAVE_ATTENDANCE_LOG = "SaveAttendanceLog";
    public static final String URL_GET_WORKING_LOG_DAYS = "http://192.168.1.59/CyberEngineService_Staging/Service_Mobile.svc/GetSumOfHoursByDateRange_Mob";
    public static final String URL_GET_PROJECT_SUBPROJECT_LIST = "http://192.168.1.59/CyberEngineService_Staging/Service_Mobile.svc/GetProjectWiseSubProjectListSelect_Mob";
    public static final String URL_GET_PROJECT_LOG_DETAILS = "http://192.168.1.59/CyberEngineService_Staging/Service_Mobile.svc/GetPOHoursEntrySelect_Mob";


    /**
     * Web Service Request Tags
     **/
    public static final String PARAM_USER_NAME = "user_name";
    public static final String REQ_PARAM_PASSWORD = "password";
    public static final String REQ_PARAM_DEVICE_TYPE = "device_type";
    public static final String REQ_PARAM_SIM_ID_LIST = "sim_ids";
    public static final String REQ_PARAM_DEVICE_ID = "device_id";
    public static final String PARAM_USER_ID = "user_id";
    public static final String REQ_PARAM_LAT = "lat";
    public static final String REQ_PARAM_LON = "lon";
    public static final String REQ_PARAM_CHECK_IN_STATUS = "check_in_status";
    public static final String REQ_PARAM_MOBILE_DATE_TIME = "mobile_date_time";
    public static final String REQ_PARAM_MOBILE_STATUS = "mobile_status";
    public static final String PARAM_PROJECT_NAME = "project_name";
    public static final String PARAM_LOG_DATE = "log_date";


    /**
     * Web Service Response Tags
     **/
    public static final String RES_PARAM_STATUS = "status";
    public static final String RES_PARAM_RESULT = "result";
    public static final String RES_PARAM_ERROR_CODE = "error_code";
    public static final String RES_PARAM_ERROR_MESSAGE = "error_message";
    public static final String RES_PARAM_DEPARTMENT_ID = "department_id";
    public static final String RES_PARAM_DESIGNATION_ID = "designation_id";
    public static final String RES_PARAM_BAND_ID = "band_id";
    public static final String RES_PARAM_USER_FULL_NAME = "full_name";
    public static final String RES_PARAM_USER_PHOTO = "photo";
    public static final String RES_PARAM_IS_CHECKED_IN = "is_checked_in";
    public static final String RES_PARAM_CHECKIN_TYPE = "checkin_type";
    public static final String RES_PARAM_MSG = "msg";
    public static final String RES_PARAM_IS_SAVED = "is_saved";
}
