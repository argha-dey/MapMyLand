package com.adey.glamorous.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.adey.glamorous.R;
import com.adey.glamorous.utility.Constants;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /********** Util variables **********/
    private Context mContext;
  //  private Prefs mPrefs;
  //  private VolleyTaskManager volleyTaskManager;
 //   private CyberEngineDataBaseHelper cyberEngineDataBaseHelper;
    private static boolean activeActivity = false;
    private String latitude, longitude;
    /********** View variables **********/
    private DrawerLayout drawer_layout;
    private FrameLayout frame_container;
    private NavigationView nav_view;
    private Switch switchCheckInCheckOut;
    private TextView tvSwitchCheckIn;
    private MenuItem checkedMenuItem; // To control last item that was checked.


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_nav_drawer);
    //    actionbar.setElevation(Util.convertDpToPixel(6, this));

        initView();
   //     Util.checkAndRequestAllPermissions(mContext);
    }


    @Override
    protected void onStart() {
        super.onStart();
        activeActivity = true;
    }


    @Override
    protected void onStop() {
        activeActivity = false;
        super.onStop();
    }


    private void initView() {
        mContext = BaseActivity.this;
      //  mPrefs = new Prefs(mContext);
      //  volleyTaskManager = new VolleyTaskManager(mContext);
        drawer_layout = findViewById(R.id.drawer_layout);
        frame_container = findViewById(R.id.frame_container);
        nav_view = findViewById(R.id.nav_view);
   //     setNavHeaderData();
        addMenuItemsInNavDrawer();
        nav_view.setNavigationItemSelectedListener(this);

        // Database init...
      //  cyberEngineDataBaseHelper = new CyberEngineDataBaseHelper(mContext);
     //   cyberEngineDataBaseHelper.deleteAllAttendance(1);
    }


    private String getAppVersion() {
        String version = "0.0";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }


    private void addMenuItemsInNavDrawer() {
        Menu menu = nav_view.getMenu();

        MenuItem home = menu.add(Menu.NONE, Constants.HOME_ITEM_ID, Menu.NONE, Constants.HOME);
        home.setCheckable(true);
        home.setIcon(R.drawable.home);

        MenuItem attendanceHistory = menu.add(Menu.NONE, Constants.ATTENDANCE_HISTORY_ITEM_ID, Menu.NONE, Constants.ATTENDANCE_HISTORY);
        attendanceHistory.setIcon(R.drawable.attendance_history);
        attendanceHistory.setCheckable(true);

        MenuItem poLog = menu.add(Menu.NONE, Constants.PO_ENTRY_ITEM_ID, Menu.NONE, Constants.PO_ENTRY);
        poLog.setIcon(R.drawable.attendance_history);
        poLog.setCheckable(true);

        MenuItem settings = menu.add(1, Constants.SETTINGS_ITEM_ID, Menu.NONE, Constants.SETTINGS);
        settings.setCheckable(true);
        settings.setIcon(R.drawable.settings);
        settings.setActionView(R.layout.nav_menu_item_with_image);

        MenuItem logout = menu.add(1, Constants.LOGOUT_ITEM_ID, Menu.NONE, Constants.LOGOUT);
        logout.setIcon(R.drawable.logout);
        logout.setVisible(false);

        nav_view.invalidate();
        // Set the Home item as the default checked item when app starts
        checkedMenuItem = home;
        checkedMenuItem.setChecked(true);
        onNavigationItemSelected(checkedMenuItem);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getTitle().toString()) {
            case Constants.HOME: {
                if (item != checkedMenuItem) {
                    checkedMenuItem.setChecked(false);
                    item.setChecked(true);
                    checkedMenuItem = item;
                }

         //       Fragment newFragment = new HomeFragment(mContext);
             //   FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
           //     ft.replace(R.id.frame_container, newFragment).commit();

                getSupportActionBar().setTitle(Constants.HOME);
            }

            // Close drawer when item is tapped
            drawer_layout.closeDrawer(Gravity.START);
            break;

            case Constants.ATTENDANCE_HISTORY: {
                if (item != checkedMenuItem) {
                    checkedMenuItem.setChecked(false);
                    item.setChecked(true);
                    checkedMenuItem = item;
                }

              //  Fragment newFragment = new AttendanceHistoryFragment(mContext);
              //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
              //  ft.replace(R.id.frame_container, newFragment).commit();

                getSupportActionBar().setTitle(Constants.ATTENDANCE_HISTORY);
            }

            // Close drawer when item is tapped
            drawer_layout.closeDrawer(Gravity.START);
            break;

            case Constants.PO_ENTRY: {
                if (item != checkedMenuItem) {
                    checkedMenuItem.setChecked(false);
                    item.setChecked(true);
                    checkedMenuItem = item;
                }

            //    Fragment newFragment = new POEntryListFragment(mContext);
            //    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
             //   ft.replace(R.id.frame_container, newFragment).commit();

                getSupportActionBar().setTitle(Constants.PO_ENTRY);
            }

            // Close drawer when item is tapped
            drawer_layout.closeDrawer(Gravity.START);
            break;

            //
            case Constants.SETTINGS:
                boolean isOpen = nav_view.getMenu().findItem(Constants.LOGOUT_ITEM_ID).isVisible();
                ImageView iv_right_arrow = item.getActionView().findViewById(R.id.iv_right_arrow);
                if (isOpen) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryText)));

                    iv_right_arrow.setRotation(-90);
                    iv_right_arrow.setColorFilter(getResources().getColor(R.color.colorSecondaryText));
                } else {
                    item.setIcon(R.drawable.settings_activated);
                    iv_right_arrow.setRotation(0);
                    iv_right_arrow.setColorFilter(getResources().getColor(R.color.colorPrimaryText));
                }

                nav_view.getMenu().findItem(Constants.LOGOUT_ITEM_ID).setVisible(!isOpen);
                break;

            case Constants.LOGOUT:
            //    showLogoutDialog();
                break;
        }

        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_layout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*
    private void setNavHeaderData() {
        if (!mPrefs.getUserImageLink().isEmpty()) {
            final ImageView ivUserImage = nav_view.getHeaderView(0).findViewById(R.id.ivUserImage);
            Picasso.get().load(mPrefs.getUserImageLink())
                    //.resize(w, h)
                    .into(ivUserImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) ivUserImage.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                            ivUserImage.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError(Exception e) {
                            ivUserImage.setImageResource(R.drawable.user_image_default);
                        }
                    });
        }
        TextView tvUserName = nav_view.getHeaderView(0).findViewById(R.id.tvUserName);
     //   tvUserName.setText(mPrefs.getFullUserName());
        TextView tvUserEmail = nav_view.getHeaderView(0).findViewById(R.id.tvUserEmail);
      //  tvUserEmail.setText(mPrefs.getUserName());
        switchCheckInCheckOut = nav_view.getHeaderView(0).findViewById(R.id.switchCheckIn);
      //  switchCheckInCheckOut.setChecked(mPrefs.getCheckInStatus());
      *//*  switchCheckInCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareCheckInCheckOutProcedure(switchCheckInCheckOut.isChecked());
            }
        });*//*
        tvSwitchCheckIn = nav_view.getHeaderView(0).findViewById(R.id.tvSwitchCheckIn);
        if (switchCheckInCheckOut.isChecked())
            tvSwitchCheckIn.setText(R.string.punchout);
        else
            tvSwitchCheckIn.setText(R.string.punchin);

        TextView tv_version = nav_view.findViewById(R.id.tv_version);
        tv_version.setText("version " + getAppVersion());
        // Call to check the check-in status
        fetchCheckInCheckOutStatus();
    }*/


    public void setContentLayout(int resId) {
        frame_container.removeAllViews();
        frame_container.addView(LayoutInflater.from(mContext).inflate(resId, frame_container, false));
    }


   /* private void showLogoutDialog() {
        // First check if user has checked-in, then ask to check-out before showLogoutDialog
        if (mPrefs.getCheckInStatus())
            Util.showAlertDialogOk(BaseActivity.this, "Please Check-out first in order to Logout.");
        else
            Util.showAlertDialogOkCancelWithCallback(BaseActivity.this,
                    "Do you want to Logout?",
                    new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            logout();
                        }

                        @Override
                        public void onCancel() {}
                    });
    }*/


/*    public void logout() {
        mPrefs.clearUserData();
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }*/


    /*public void fetchCheckInCheckOutStatus() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
        paramsMap.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(mContext));
        volleyTaskManager.fetchCheckInCheckOutStatus(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @SuppressLint("NewApi")
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                    JSONObject result = resultJsonObject.optJSONObject(WebServiceConstants.RES_PARAM_RESULT);
                    if (result != null) {
                        mPrefs.setCheckInType(result.optInt(WebServiceConstants.RES_PARAM_CHECKIN_TYPE));
                        if (result.optBoolean(WebServiceConstants.RES_PARAM_IS_CHECKED_IN)) {
                            if (mPrefs.getCheckInType() == AppConstants.CHECKIN_TYPE_APP) {
                                // If checked-in from app, then show manual attendance notifications after 6:45pm and start auto attendance system
                                startAutoAttendanceService(mContext);
                                startAttendanceAfterDayEnd(mContext);
                            }
                            mPrefs.setCheckInStatus(AppConstants.CHECKIN);
                        } else {
                            mPrefs.setCheckInStatus(AppConstants.CHECKOUT);
                        }
                        switchCheckInCheckOut.setChecked(mPrefs.getCheckInStatus());
                        if (switchCheckInCheckOut.isChecked())
                            tvSwitchCheckIn.setText(R.string.punchout);
                        else
                            tvSwitchCheckIn.setText(R.string.punchin);
                    }
                } else {
                    if (resultJsonObject.optInt(WebServiceConstants.RES_PARAM_ERROR_CODE) == AppConstants.ERROR_CODE_AUTHENTICATION_FAILURE) {
                        logout();
                        return;
                    }
                    switchCheckInCheckOut.setChecked(mPrefs.getCheckInStatus());
                    if (switchCheckInCheckOut.isChecked())
                        tvSwitchCheckIn.setText(R.string.punchout);
                    else
                        tvSwitchCheckIn.setText(R.string.punchin);
                }
                updateCurrentFragment();
            }

            @Override
            public void onError() {
            }
        });
    }*/


  /*  public void prepareCheckInCheckOutProcedure(final boolean checkIn) {
        switchCheckInCheckOut.setChecked(!checkIn);
        if (Util.checkConnectivity(mContext)) {
            if (Util.checkGpsStatus(mContext)) {
                if (Util.checkGpsHighAccuracyMode(mContext)) {
                    if (Util.isMockLocationOff(mContext)) {
                        final ProgressDialog progressDialog = Util.showProgressDialog(mContext, "Fetching location...");
                        progressDialog.show();
                        new GetLocationService().getCurrentLocation(mContext, new CELocationCallback() {
                            @Override
                            public void onLocationFetchCompleted(Location location) {
                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                                progressDialog.cancel();
                                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                                    String msg;
                                    if (checkIn) msg = "Are you sure you want to Check-in?";
                                    else msg = "Are you sure you want to Check-out?";
                                    Util.showAlertDialogOkCancelWithCallback(mContext, msg, new AlertDialogCallBack() {
                                        @Override
                                        public void onSubmit() {
                                            if (checkIn) callCheckInAPI();
                                            else callCheckOutAPI();
                                        }

                                        @Override
                                        public void onCancel() {}
                                    });
                                } else
                                    Util.showAlertDialogOk(mContext, "Please turn on the GPS/Location service.");
                            }
                        });
                    } else
                        Util.showAlertDialogOk(mContext, "Location Mock/Spoofing function is enabled. Please turn off that before using this application.");
                } else new GetLocationService().showSettingsAlertForHighAccuracy(mContext);
            } else
                new GetLocationService().showSettingsAlert(mContext);
        } else
            Util.showAlertDialogOk(mContext, "Please check the internet connection.");
    }*/

/*
    private void callCheckInAPI() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_LAT, latitude);
        paramsMap.put(WebServiceConstants.REQ_PARAM_LON, longitude);
        paramsMap.put(WebServiceConstants.REQ_PARAM_CHECK_IN_STATUS, AppConstants.CHECKIN);

        volleyTaskManager.checkInCheckOut(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @SuppressLint("NewApi")
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {

                    mPrefs.setCheckInStatus(AppConstants.CHECKIN);
                    mPrefs.setCheckInType(AppConstants.CHECKIN_TYPE_APP);

                    startAutoAttendanceService(mContext);
                    // If checked-in from app, then show manual attendance notifications after 6:45pm
                    startAttendanceAfterDayEnd(mContext);

                    switchCheckInCheckOut.setChecked(mPrefs.getCheckInStatus());
                    if (switchCheckInCheckOut.isChecked())
                        tvSwitchCheckIn.setText(R.string.punchout);
                    else
                        tvSwitchCheckIn.setText(R.string.punchin);
                    Toast.makeText(mContext, "Successful check-in.", Toast.LENGTH_LONG).show();
                    updateCurrentFragment();
                } else {
                    if (resultJsonObject.optInt(WebServiceConstants.RES_PARAM_ERROR_CODE) == AppConstants.ERROR_CODE_AUTHENTICATION_FAILURE) {
                        logout();
                        return;
                    }
                    Toast.makeText(mContext, resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "Check-in failed due to Volley error. Try again.", Toast.LENGTH_LONG).show();
            }
        });
    }*/


    /*private void callCheckOutAPI() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_LAT, latitude);
        paramsMap.put(WebServiceConstants.REQ_PARAM_LON, longitude);
        paramsMap.put(WebServiceConstants.REQ_PARAM_CHECK_IN_STATUS, AppConstants.CHECKOUT);

        volleyTaskManager.checkInCheckOut(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {

                    mPrefs.setCheckInStatus(AppConstants.CHECKOUT);
                    mPrefs.setCheckInType(0);

                    if (Util.checkConnectivity(mContext)) {
                        Intent serviceIntent = new Intent(mContext, AutoAttendanceOfflineDataSendService.class);
                        mContext.startService(serviceIntent);
                    }

                    stopAutoAttendanceService(mContext);
                    if (mPrefs.getCheckInType() == AppConstants.CHECKIN_TYPE_APP) {
                        // If checked-in from app, then show manual attendance notifications after 6:45pm
                        stopAttendanceAfterDayEnd(mContext);
                    }

                    switchCheckInCheckOut.setChecked(mPrefs.getCheckInStatus());
                    if (switchCheckInCheckOut.isChecked())
                        tvSwitchCheckIn.setText(R.string.punchout);
                    else
                        tvSwitchCheckIn.setText(R.string.punchin);
                    Toast.makeText(mContext, "Successful check-out.", Toast.LENGTH_LONG).show();
                    updateCurrentFragment();
                } else {
                    if (resultJsonObject.optInt(WebServiceConstants.RES_PARAM_ERROR_CODE) == AppConstants.ERROR_CODE_AUTHENTICATION_FAILURE) {
                        logout();
                        return;
                    }
                    Toast.makeText(mContext, resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "Check-out failed due to Volley error. Try again.", Toast.LENGTH_LONG).show();
            }
        });
    }*/


 /*   private void startAttendanceAfterDayEnd(Context context) {
        Intent intent = new Intent(context, AttendanceAfterDayEndBroadcastReceiver.class);
        intent.putExtra(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        // Get a Calendar and set the time to 6:45:00
        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        //alarmStartTime.set(Calendar.HOUR_OF_DAY, 16);
        //alarmStartTime.set(Calendar.MINUTE, 5);
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
        alarmStartTime.set(Calendar.MINUTE, 45);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, Constants.ATTENDANCE_AFTER_DAY_END_ALERT_REQ, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), (60 * 1000), pendingIntent);  // for testing every one min
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), Constants.ONE_HOUR_INTERVAL, pendingIntent); // for one hour = 60 min.
        }
    }*/


    /*private void stopAttendanceAfterDayEnd(Context context) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            Intent intentAlarm = new Intent(context, AttendanceAfterDayEndBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.ATTENDANCE_AFTER_DAY_END_ALERT_REQ, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }


    private void startAutoAttendanceService(Context context) {
        Intent intent = new Intent(context, AutoAttendanceService.class);
        PendingIntent autoIntent = PendingIntent.getService(mContext, Constants.AUTO_ATTENDANCE_ALERT_REQ, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, (Calendar.getInstance().getTimeInMillis() + Constants.ONE_HOUR_INTERVAL), Constants.ONE_HOUR_INTERVAL, autoIntent); // 60 min = 1h
        }
    }


    private void stopAutoAttendanceService(Context context) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            Intent intentAlarm = new Intent(context, AutoAttendanceService.class);
            PendingIntent pendingIntent = PendingIntent.getService(mContext, Constants.AUTO_ATTENDANCE_ALERT_REQ, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }*/

/*

    private void updateCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (currentFragment != null && currentFragment instanceof BaseFragment)
            ((BaseFragment) currentFragment).onCheckInStatusChange();
    }
*/

}
