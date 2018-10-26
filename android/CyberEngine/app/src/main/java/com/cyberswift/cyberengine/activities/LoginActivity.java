package com.cyberswift.cyberengine.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.listeners.AlertDialogCallBack;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.services.ServiceConnector;
import com.cyberswift.cyberengine.services.VolleyTaskManager;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Constants;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.Util;
import com.cyberswift.cyberengine.utility.WebServiceConstants;
import com.rey.material.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private Context mContext;
    private EditText et_username, et_password;
    private CheckBox checkbox_rememberMe;
    private Button btn_login;
    private VolleyTaskManager volleyTaskManager;
    private Prefs mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        initView();
    }


    private void initView() {
        mPrefs = new Prefs(mContext);
        volleyTaskManager = new VolleyTaskManager(mContext);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        et_username.setText(mPrefs.getUserName());
        et_password.setText(mPrefs.getPassword());
        checkbox_rememberMe = findViewById(R.id.checkbox_rememberMe);
        checkbox_rememberMe.setOnClickListener(this);

        if (mPrefs.getRememberMeChecked()) {
            checkbox_rememberMe.setChecked(true);
        } else {
            checkbox_rememberMe.setChecked(false);
        }

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_username.setTextColor(getResources().getColor(R.color.colorPrimary));
                    et_username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_activated, 0, 0, 0);
                } else {
                    et_username.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                    et_username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_normal, 0, 0, 0);
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_password.setTextColor(getResources().getColor(R.color.colorPrimary));
                    et_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_activated, 0, 0, 0);
                } else {
                    et_password.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                    et_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_normal, 0, 0, 0);
                }
            }
        });
    }


    @Override
    public void onRestart() {
        super.onRestart();
        mContext = LoginActivity.this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (et_username.getText().toString().length() == 0 || et_password.getText().toString().length() == 0) {
                    Util.showAlertDialogOk(mContext, "Please fill all the required fields.");
                } else login();

                break;

            case R.id.checkbox_rememberMe:
                if (checkbox_rememberMe.isChecked()) {
                    mPrefs.setRememberMeChecked(true);

                    mPrefs.setUserName(et_username.getText().toString());
                    mPrefs.setPassword(et_password.getText().toString());
                } else {
                    mPrefs.setRememberMeChecked(false);
                    mPrefs.setUserName("");
                    mPrefs.setPassword("");
                }
                break;

            default:
                break;
        }
    }


    /*********************
     * Method to call Login WebService
     *********************/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MULTIPLE_PERMISSIONS_REQ: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    if (perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        login();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Util.showAlertDialogOkCancelWithCallback(mContext, "All necessary permissions are required to use this application.", new AlertDialogCallBack() {
                                @Override
                                public void onSubmit() {
                                    Util.checkAndRequestAllPermissions(mContext);
                                }

                                @Override
                                public void onCancel() {}
                            });
                        } else {
                            Toast.makeText(this, "Go to settings and enable all necessary permissions.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }


    private void login() {
        if (Util.checkConnectivity(mContext)) {
            if (Util.checkAndRequestAllPermissions(mContext)) {
                JSONArray simID = Util.getMobileSimIdArr(mContext);
                if (simID.length() > 0) {
                    loginWebserviceCalling();
                } else
                    Util.showAlertDialogOk(mContext, "SIM cards not available!");
            }
        } else
            Util.showAlertDialogOk(LoginActivity.this, getString(R.string.no_internet));
    }


    private void loginWebserviceCalling() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.PARAM_USER_NAME, et_username.getText().toString());
        paramsMap.put(WebServiceConstants.REQ_PARAM_PASSWORD, et_password.getText().toString());
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
        volleyTaskManager.doLogin(new JSONObject(paramsMap), true, new ServerResponseCallback() {

            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                    JSONObject jObj = resultJsonObject.optJSONObject(WebServiceConstants.RES_PARAM_RESULT);
                    if (jObj != null) {
                        mPrefs.setLoginStatus(true);
                        mPrefs.setUserId(jObj.optInt(WebServiceConstants.PARAM_USER_ID));
                        mPrefs.setUserName(jObj.optString(WebServiceConstants.PARAM_USER_NAME));
                        mPrefs.setUserBandId(jObj.optInt(WebServiceConstants.RES_PARAM_BAND_ID));
                        mPrefs.setUserDepartmentId(jObj.optInt(WebServiceConstants.RES_PARAM_DEPARTMENT_ID));
                        mPrefs.setUserDesignationId(jObj.optInt(WebServiceConstants.RES_PARAM_DESIGNATION_ID));
                        mPrefs.setFullUserName(jObj.optString(WebServiceConstants.RES_PARAM_USER_FULL_NAME));
                        mPrefs.setUserImageLink(ServiceConnector.getBaseImageURL() + jObj.optString(WebServiceConstants.RES_PARAM_USER_PHOTO));

                        startBaseActivity();
                    }
                } else {
                    String msg = "Unexpected error. Please try again.";
                    if (resultJsonObject.opt(WebServiceConstants.RES_PARAM_ERROR_MESSAGE) != null)
                        msg = resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE);

                    Util.showAlertDialogOk(mContext, msg);
                }
            }

            @Override
            public void onError() {}
        });
    }


    /*********************
     * GO TO Home *
     *********************/
    private void startBaseActivity() {
        startActivity(new Intent(LoginActivity.this, BaseActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }
}
