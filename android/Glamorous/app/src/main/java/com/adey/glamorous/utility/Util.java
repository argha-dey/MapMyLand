package com.adey.glamorous.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.listeners.AlertDialogCallBack;
import com.cyberswift.cyberengine.models.UserCurrentState;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private static String USER_CURRENT_STATUS = "USER_CURRENT_STATUS";
    private static String PREF_NAME = "CEPref";


    public static boolean checkConnectivity(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.v("INTERNET: ", String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.v("INTERNET: ", "Connected");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static void showAlertDialogOk(final Context mContext, final String message) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.CustomDialog);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                String first = mContext.getResources().getString(R.string.app_name_first_part);
                builder.append(first);
                String second = mContext.getResources().getString(R.string.app_name_second_part);
                SpannableString spannable = new SpannableString(second);
                spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorSecondaryText)), 0, second.length(), 0);
                builder.append(spannable);
                alert.setTitle(builder);

                alert.setMessage(message);
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                alert.show();
            }
        });
    }


    public static void showAlertDialogOkWithCallback(final Context mContext, final String message, final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.CustomDialog);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                String first = mContext.getResources().getString(R.string.app_name_first_part);
                builder.append(first);
                String second = mContext.getResources().getString(R.string.app_name_second_part);
                SpannableString spannable = new SpannableString(second);
                spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorSecondaryText)), 0, second.length(), 0);
                builder.append(spannable);
                alert.setTitle(builder);

                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBack.onSubmit();
                            }
                        });
                alert.show();
            }
        });
    }


    public static void showAlertDialogOkCancelWithCallback(final Context mContext, final String message, final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.CustomDialog);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                String first = mContext.getResources().getString(R.string.app_name_first_part);
                builder.append(first);
                String second = mContext.getResources().getString(R.string.app_name_second_part);
                SpannableString spannable = new SpannableString(second);
                spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorSecondaryText)), 0, second.length(), 0);
                builder.append(spannable);
                alert.setTitle(builder);

                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBack.onSubmit();
                            }
                        });
                alert.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBack.onCancel();
                            }
                        });

                alert.show();
            }
        });
    }


    // Saving UserClass details
    public static void saveUserCurrentState(final Context mContext, UserCurrentState userClass) {
        SharedPreferences IMISPrefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = IMISPrefs.edit();
        try {
            prefsEditor.putString(USER_CURRENT_STATUS, ObjectSerializer.serialize(userClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }


    // Fetching UserClass details
    public static UserCurrentState fetchUserCurrentState(final Context mContext) {
        SharedPreferences IMISPrefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        UserCurrentState userClass = null;
        String serializeOrg = IMISPrefs.getString(USER_CURRENT_STATUS, null);
        try {
            if (serializeOrg != null) {
                userClass = (UserCurrentState) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return userClass;
    }


    /**
     * Checking permission request
     */
    public static boolean checkAndRequestAllPermissions(final Context context) {

        int permissionPhoneState = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE);
        int locationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.MULTIPLE_PERMISSIONS_REQ);
            return false;
        }

        return true;
    }


    public static String getMobileDeviceID(final Context context) {
        String device_ID = "";
        try {
            //device_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            device_ID = "1111";
        } catch (Exception e) {
            Toast.makeText(context, "Device ID not available.", Toast.LENGTH_LONG).show();
        }
        return device_ID;
    }


    @SuppressLint({"MissingPermission"})
    public static JSONArray getMobileSimIdArr(final Context context) {
        JSONArray simIdArr = new JSONArray();
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager manager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                List<SubscriptionInfo> infoList = manager.getActiveSubscriptionInfoList();
                if (infoList != null && !infoList.isEmpty()) {
                    for (int i = 0; i < infoList.size(); i++) {
                        SubscriptionInfo info = infoList.get(i);
                        simIdArr.put(info.getIccId());
                        //System.out.println("SubscriptionInfo : " + info.getSimSlotIndex() + " -> " + info.getIccId());
                    }
                }
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                simIdArr.put(telephonyManager.getSimSerialNumber());
            }

            simIdArr.put("1111");
        } catch (Exception ex) {
            Toast.makeText(context, "Invalid SIM or SIM not available.", Toast.LENGTH_LONG).show();
        }
        return simIdArr;
    }


    public static boolean checkGpsStatus(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null ? locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) : false;
    }


    public static boolean checkGpsHighAccuracyMode(Context mContext) {
        int location = 0;
        boolean GpsAccuracy = false;
        try {
            location = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);
            if (location == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                GpsAccuracy = true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return GpsAccuracy;
    }


    public static boolean isMockLocationOff(Context context) {
        // Returns true if mock location enabled, false if not enabled.
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
    }


    public static ProgressDialog showProgressDialog(final Context context, final String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        return progressDialog;
    }


    public static int convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public static float convertPixelToDp(int pixel, Context context) {
        return pixel / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }





}
