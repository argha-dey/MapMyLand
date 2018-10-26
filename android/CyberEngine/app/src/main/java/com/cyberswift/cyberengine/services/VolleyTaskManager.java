package com.cyberswift.cyberengine.services;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.application.AppController;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


@SuppressLint("ShowToast")
public class VolleyTaskManager extends ServiceConnector {

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String TAG;
    private String JSON_REQ = "jobj_req";
    private boolean isToShowDialog = true, isToHideDialog = true;


    public VolleyTaskManager(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");
        TAG = mContext.getClass().getSimpleName();
    }


    private void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    /**
     * ***************** Making json object request *****************
     **/
    private void makeJsonObjReq(int method, String url, final Map<String, String> paramsMap) {
        makeJsonObjReqWithCallBack(method, url, new JSONObject(paramsMap), null);
    }


    private void makeJsonObjReq(int method, String url, final JSONObject params) {
        makeJsonObjReqWithCallBack(method, url, params, null);
    }


    private void makeJsonObjReqWithCallBack(int method, String url, final Map<String, String> paramsMap, final ServerResponseCallback serverResponseCallback) {
        makeJsonObjReqWithCallBack(method, url, new JSONObject(paramsMap), serverResponseCallback);
    }


    private void makeJsonObjReqWithCallBack(int method, String url, final JSONObject params, final ServerResponseCallback serverResponseCallback) {
        if (isToShowDialog) {
            showProgressDialog();
        }

        Log.d(TAG, url);
        Log.d(TAG, params.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        //msgResponse.setText(response.toString());
                        if (isToHideDialog) {
                            hideProgressDialog();
                        }

                        if (serverResponseCallback != null)
                            serverResponseCallback.onSuccess(response);
                        else
                            ((ServerResponseCallback) mContext).onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Log.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(mContext, mContext.getString(R.string.response_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mContext, mContext.getString(R.string.auth_failure),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mContext, mContext.getString(R.string.server_error),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(mContext, mContext.getString(R.string.network_error),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(mContext, mContext.getString(R.string.parse_error),
                            Toast.LENGTH_LONG).show();
                }
                if (serverResponseCallback != null)
                    serverResponseCallback.onError();
                else
                    ((ServerResponseCallback) mContext).onError();
            }
        }) {

            /**
             * Passing some request headers
             **/
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, JSON_REQ);

        // Cancelling request
        //ApplicationController.getInstance().getRequestQueue().cancelAll(JSON_REQ);
    }


    /**
     * Service method calling for Login -->
     **/
    public void doLogin(JSONObject params, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + WebServiceConstants.URL_GET_MOB_LOGIN;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for Check-In/Check-Out -->
     **/
    public void checkInCheckOut(JSONObject params, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + WebServiceConstants.URL_CHECKIN_CHECKOUT;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for getting the Check-In/Check-Out status of the current day -->
     **/
    public void fetchCheckInCheckOutStatus(JSONObject params, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + WebServiceConstants.URL_GET_CHECKIN_CHECKOUT_STATUS;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for getting the Check-In/Check-Out data list of the current day -->
     **/
    public void getCurrentDateAttendanceList(JSONObject params, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + WebServiceConstants.URL_GET_CHECKIN_CHECKOUT_LIST;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for getting the Check-In/Check-Out data list of the current month -->
     **/
    public void getCurrentMonthAttendanceList(JSONObject params, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + WebServiceConstants.URL_GET_CURRENT_MONTH_ATTENDANCE_LIST;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for sending offline auto attendance data to server AND
     * Continue Attendance online through notification -->
     **/
    public void sendAttendanceLog(JSONObject params, boolean isToShowDialog, ServerResponseCallback serverResponseCallback) {
        this.isToShowDialog = isToShowDialog;
        String url = getBaseURL() + WebServiceConstants.URL_SAVE_ATTENDANCE_LOG;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for
     **/
    public void getWorkingLogDays(JSONObject params, boolean isToShowDialog, ServerResponseCallback serverResponseCallback) {
        this.isToShowDialog = isToShowDialog;
//        String url = getBaseURL() + WebServiceConstants.URL_SAVE_ATTENDANCE_LOG;
        String url = WebServiceConstants.URL_GET_WORKING_LOG_DAYS;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for
     **/
    public void getProjectAndSubProjectList(JSONObject params, boolean isToShowDialog, ServerResponseCallback serverResponseCallback) {
        this.isToShowDialog = isToShowDialog;
//        String url = getBaseURL() + WebServiceConstants.URL_SAVE_ATTENDANCE_LOG;
        String url = WebServiceConstants.URL_GET_PROJECT_SUBPROJECT_LIST;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


    /**
     * Service method calling for
     **/
    public void getProjectLogDetails(JSONObject params, boolean isToShowDialog, ServerResponseCallback serverResponseCallback) {
        this.isToShowDialog = isToShowDialog;
//        String url = getBaseURL() + WebServiceConstants.URL_SAVE_ATTENDANCE_LOG;
        String url = WebServiceConstants.URL_GET_PROJECT_LOG_DETAILS;
        makeJsonObjReqWithCallBack(Method.POST, url, params, serverResponseCallback);
    }


}
