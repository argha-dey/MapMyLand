package com.cyberswift.cyberengine.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

@SuppressLint("ApplySharedPref")
public class Prefs {

    private Context context;


    public Prefs(Context context) {
        this.context = context;
    }

    private String getString(String key, String def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String s = prefs.getString(key, def);
        return s;
    }

    private void setString(String key, String val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putString(key, val);
        e.commit();
    }

    private int getInt(String key, int def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, def);
    }

    private void setInt(String key, int val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putInt(key, val);
        e.commit();
    }

    private boolean getBoolean(String key, boolean def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, def);
    }

    private void setBoolean(String key, boolean val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putBoolean(key, val);
        e.commit();
    }


    public boolean getCheckInStatus() {
        return getBoolean(Constants.CHECKIN_STATUS, false); // true means checked-in, false means checked-out
    }

    public void setCheckInStatus(boolean val) {
        setBoolean(Constants.CHECKIN_STATUS, val); // true means checked-in, false means checked-out
    }

    public int getCheckInType() {
        return getInt(Constants.CHECKIN_TYPE, 0);
    }

    public void setCheckInType(int val) {
        setInt(Constants.CHECKIN_TYPE, val);
    }

    public boolean getLoginStatus() {
        return getBoolean(Constants.LOGIN_STATUS, false);
    }

    public void setLoginStatus(boolean val) {
        setBoolean(Constants.LOGIN_STATUS, val);
    }

    public String getUserName() {
        return getString(Constants.USER_NAME, "");
    }

    public void setUserName(String val) {
        setString(Constants.USER_NAME, val);
    }

    public String getPassword() {
        return getString(Constants.PASSWORD, "");
    }

    public void setPassword(String val) {
        setString(Constants.PASSWORD, val);
    }

    public boolean getRememberMeChecked() {
        return getBoolean(Constants.REMEMBER_ME_CHECKED, false);
    }

    public void setRememberMeChecked(boolean val) {
        setBoolean(Constants.REMEMBER_ME_CHECKED, val);
    }

    public int getUserId() {
        return getInt(Constants.USER_ID, 0);
    }

    public void setUserId(int val) {
        setInt(Constants.USER_ID, val);
    }

    public int getUserBandId() {
        return getInt(Constants.BAND_ID, 0);
    }

    public void setUserBandId(int val) {
        setInt(Constants.BAND_ID, val);
    }

    public int getUserDepartmentId() {
        return getInt(Constants.DEPT_ID, 0);
    }

    public void setUserDepartmentId(int val) {
        setInt(Constants.DEPT_ID, val);
    }

    public int getUserDesignationId() {
        return getInt(Constants.DESIG_ID, 0);
    }

    public void setUserDesignationId(int val) {
        setInt(Constants.DESIG_ID, val);
    }

    public String getFullUserName() {
        return getString(Constants.FULL_NAME, "");
    }

    public void setFullUserName(String val) {
        setString(Constants.FULL_NAME, val);
    }

    public String getUserImageLink() {
        return getString(Constants.USER_PHOTO, "");
    }

    public void setUserImageLink(String val) {
        setString(Constants.USER_PHOTO, val);
    }

    public void clearUserData() {
        //setUserName(""); // This is not deleted to show the previous login cred to the user in the login Activity.
        //setPassword(""); // This is not deleted to show the previous login cred to the user in the login Activity.
        setUserId(0);
        setUserBandId(0);
        setUserDepartmentId(0);
        setUserDesignationId(0);
        setFullUserName("");
        setUserImageLink("");
        setLoginStatus(false);
    }

    public void clearPrefsData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.clear().commit();
    }
}
