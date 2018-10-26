package com.cyberswift.cyberengine.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cyberswift.cyberengine.services.AutoAttendanceService;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Constants;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class BootReceiver extends BroadcastReceiver {

    private Prefs mPrefs;


    @Override
    public void onReceive(Context mContext, Intent intent) {
        mPrefs = new Prefs(mContext);

        if (mPrefs.getCheckInStatus()) {
            startAutoAttendanceService(mContext);
            if (mPrefs.getCheckInType() == AppConstants.CHECKIN_TYPE_APP) {
                startAttendanceAfterDayEndService(mContext);
            }
        }
    }


    private void startAutoAttendanceService(Context mContext) {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(mContext, AutoAttendanceService.class);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, Constants.AUTO_ATTENDANCE_ALERT_REQ, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), Constants.ONE_HOUR_INTERVAL, pendingIntent); // Auto attendance data log after reboot
    }


    private void startAttendanceAfterDayEndService(Context mContext) {
        Intent intent = new Intent(mContext, AttendanceAfterDayEndBroadcastReceiver.class);
        intent.putExtra(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        // Get a Calendar and set the time to 6:45:00 pm
        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
        alarmStartTime.set(Calendar.MINUTE, 45);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, Constants.ATTENDANCE_AFTER_DAY_END_ALERT_REQ, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), Constants.ONE_HOUR_INTERVAL, pendingIntent); // Start after switch off user mobile.
    }
}