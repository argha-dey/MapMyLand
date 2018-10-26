package com.cyberswift.cyberengine.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.RemoteViews;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.database.CyberEngineDataBaseHelper;
import com.cyberswift.cyberengine.listeners.CELocationCallback;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.models.AttendanceUserClass;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Constants;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.Util;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class AutoAttendanceService extends Service {

    Intent intent;
    Context context;
    VolleyTaskManager volleyTaskManager;
    private Prefs mPrefs;
    private String latitude = "", longitude = "";
    private CyberEngineDataBaseHelper cyberEngineDataBaseHelper;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        context = getApplicationContext();
        volleyTaskManager = new VolleyTaskManager(context);
        // Database...
        cyberEngineDataBaseHelper = new CyberEngineDataBaseHelper(context);
        mPrefs = new Prefs(context);
        if (Util.checkGpsStatus(context) || Util.checkConnectivity(context)) {
            if (Util.checkGpsStatus(context)) {
                if (Util.isMockLocationOff(context)) {
                    if (Util.checkConnectivity(context)) {
                        new GetLocationService().getCurrentLocation(context, new CELocationCallback() {
                            @Override
                            public void onLocationFetchCompleted(Location location) {
                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                                    sendAutoAttendanceToServer("INTERNET ON AND GPS ON");
                                }
                            }
                        });
                    } else {
                        new GetLocationService().getCurrentLocation(context, new CELocationCallback() {
                            @Override
                            public void onLocationFetchCompleted(Location location) {
                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                                    cyberEngineDataBaseHelper.addAttendanceInfo(new AttendanceUserClass(mPrefs.getUserId(), latitude, longitude, getDateAndTime(), 1, "INTERNET OFF, GPS ON"));
                                }
                            }
                        });
                    }
                } else {
                    cyberEngineDataBaseHelper.addAttendanceInfo(new AttendanceUserClass(mPrefs.getUserId(), latitude, longitude, getDateAndTime(), 1, "MOCK LOCATION ON"));
                    showCustomNotification(context, "Your Mock Location/Spoofing function is enabled. Please turn that off immediately.");
                }
            } else {
                cyberEngineDataBaseHelper.addAttendanceInfo(new AttendanceUserClass(mPrefs.getUserId(), latitude, longitude, getDateAndTime(), 1, "INTERNET ON, GPS OFF"));
                showCustomNotification(context, "Please turn on your GPS/Location service.");
            }
        } else {
            cyberEngineDataBaseHelper.addAttendanceInfo(new AttendanceUserClass(mPrefs.getUserId(), latitude, longitude, getDateAndTime(), 1, "INTERNET OFF, GPS OFF"));
        }

        return START_NOT_STICKY;
    }


    public String getDateAndTime() {
        SimpleDateFormat postFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return postFormatter.format(new Date());
    }


    private void showCustomNotification(Context context, String msg) {
        Spannable appName = new SpannableString(context.getResources().getString(R.string.app_name_first_part) + context.getResources().getString(R.string.app_name_second_part));
        appName.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondaryText)), context.getResources().getString(R.string.app_name_first_part).length()+1,
                appName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        customView.setImageViewResource(R.id.noti_image, R.drawable.notification_icon);
        customView.setTextViewText(R.id.noti_title, appName);
        customView.setTextViewText(R.id.noti_text, msg);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "AutoAttendanceService")
                .setSmallIcon(R.mipmap.notification_icon)
                .setCustomContentView(customView)
                .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.AUTO_ATTENDANCE, mBuilder.build());
    }


    private void sendAutoAttendanceToServer(String mobile_status) {
        HashMap<String, Object> requestParam = new HashMap<>();
        requestParam.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        requestParam.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(context));
        requestParam.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(context));
        requestParam.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
        requestParam.put(WebServiceConstants.REQ_PARAM_LAT, latitude);
        requestParam.put(WebServiceConstants.REQ_PARAM_LON, longitude);
        requestParam.put(WebServiceConstants.REQ_PARAM_MOBILE_DATE_TIME, getDateAndTime());
        requestParam.put(WebServiceConstants.REQ_PARAM_MOBILE_STATUS, mobile_status);

        volleyTaskManager.sendAttendanceLog(new JSONObject(requestParam), false, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (!resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS))
                    cyberEngineDataBaseHelper.addAttendanceInfo(new AttendanceUserClass(mPrefs.getUserId(), latitude, longitude, getDateAndTime(), 1, "INTERNET ON, GPS ON"));
            }

            @Override
            public void onError() {
                cyberEngineDataBaseHelper.addAttendanceInfo(new AttendanceUserClass(mPrefs.getUserId(), latitude, longitude, getDateAndTime(), 1, "INTERNET ON, GPS ON"));
            }
        });
    }
}