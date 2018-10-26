package com.cyberswift.cyberengine.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.cyberswift.cyberengine.database.CyberEngineDataBaseHelper;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.models.AttendanceUserClass;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Util;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AutoAttendanceOfflineDataSendService extends Service {

    private Context context;
    private VolleyTaskManager volleyTaskManager;
    private CyberEngineDataBaseHelper cyberEngineDataBaseHelper;
    private ArrayList<AttendanceUserClass> attendanceUser;


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
        context = getApplicationContext();
        volleyTaskManager = new VolleyTaskManager(context);
        cyberEngineDataBaseHelper = new CyberEngineDataBaseHelper(context);
        attendanceUser = new ArrayList<>();
        new FetchDataFromDatabase().execute();
        return START_NOT_STICKY;
    }

    @SuppressLint("StaticFieldLeak")
    class FetchDataFromDatabase extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... f_url) {
            attendanceUser = cyberEngineDataBaseHelper.getUserReadyToUpload(1); // for new consumer
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (attendanceUser != null) {
                if (Util.checkConnectivity(context)) {
                    new SendNewAttendanceData().execute();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SendNewAttendanceData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            for (int i = 0; i < attendanceUser.size(); i++) {
                if (i >= attendanceUser.size())
                    break;
                /*******************
                 * Using Volley
                 *******************/
                // Post params to be sent to the server
                final int localId = attendanceUser.get(i).getLocal_id();
                HashMap<String, Object> requestParam = new HashMap<>();
                requestParam.put(WebServiceConstants.PARAM_USER_ID, attendanceUser.get(i).getUser_id());
                requestParam.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(context));
                requestParam.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(context));
                requestParam.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
                requestParam.put(WebServiceConstants.REQ_PARAM_LAT, attendanceUser.get(i).getLat());
                requestParam.put(WebServiceConstants.REQ_PARAM_LON, attendanceUser.get(i).getLon());
                requestParam.put(WebServiceConstants.REQ_PARAM_MOBILE_DATE_TIME, attendanceUser.get(i).getDate_time());
                requestParam.put(WebServiceConstants.REQ_PARAM_MOBILE_STATUS, attendanceUser.get(i).getMobileStatus());

                volleyTaskManager.sendAttendanceLog(new JSONObject(requestParam), false, new ServerResponseCallback() {
                    @Override
                    public void onSuccess(JSONObject resultJsonObject) {
                        try {
                            if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                                if (resultJsonObject.optJSONObject(WebServiceConstants.RES_PARAM_RESULT) != null &&
                                        resultJsonObject.optJSONObject(WebServiceConstants.RES_PARAM_RESULT).optBoolean(WebServiceConstants.RES_PARAM_IS_SAVED))
                                    cyberEngineDataBaseHelper.updateAttendanceTableBackgroundServerResponse(0, localId);
                            } else {
                                String msg = "Unexpected error. Please try again.";
                                if (resultJsonObject.opt(WebServiceConstants.RES_PARAM_ERROR_MESSAGE) != null)
                                    msg = resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE);
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {}
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            Intent serviceIntent = new Intent(context, AutoAttendanceOfflineDataSendService.class);
            context.stopService(serviceIntent);
        }
    }
}
