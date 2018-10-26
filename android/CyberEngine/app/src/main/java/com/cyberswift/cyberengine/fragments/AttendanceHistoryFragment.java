package com.cyberswift.cyberengine.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.activities.BaseActivity;
import com.cyberswift.cyberengine.adapters.AttendanceHistoryListAdapter;
import com.cyberswift.cyberengine.listeners.CheckInCheckOutCallBack;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.services.VolleyTaskManager;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.Util;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

@SuppressLint("ValidFragment")
public class AttendanceHistoryFragment extends BaseFragment implements CheckInCheckOutCallBack {

    private Context mContext;
    private VolleyTaskManager volleyTaskManager;
    private Prefs mPrefs;
    private ImageView iv_no_data_found;
    private RecyclerView rcvAttendanceHistory;
    private AttendanceHistoryListAdapter attendanceHistoryListAdapter;


    public AttendanceHistoryFragment(Context mContext) {
        this.mContext = mContext;
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_history, container, false);
        mContext = getActivity();
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
        initView(v);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        fetchAttendanceHistory();
    }


    private void initView(View v) {
        iv_no_data_found = v.findViewById(R.id.iv_no_data_found);
        rcvAttendanceHistory = v.findViewById(R.id.rcvAttendanceHistory);
        rcvAttendanceHistory.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void fetchAttendanceHistory() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
        volleyTaskManager.getCurrentMonthAttendanceList(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                    JSONArray attendanceList = resultJsonObject.optJSONArray(WebServiceConstants.RES_PARAM_RESULT);
                    if (attendanceList != null && attendanceList.length() > 0) {
                        iv_no_data_found.setVisibility(View.GONE);
                        setAttendanceListAdapter(attendanceList);
                    } else
                        iv_no_data_found.setVisibility(View.VISIBLE);
                } else {
                    if (resultJsonObject.optInt(WebServiceConstants.RES_PARAM_ERROR_CODE) == AppConstants.ERROR_CODE_AUTHENTICATION_FAILURE) {
                        ((BaseActivity) getActivity()).logout();
                        return;
                    }
                    String msg = "Unexpected error. Please try again.";
                    if (resultJsonObject.opt(WebServiceConstants.RES_PARAM_ERROR_MESSAGE) != null)
                        msg = resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE);

                    iv_no_data_found.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {}
        });
    }

    private void setAttendanceListAdapter(JSONArray attendanceList) {
        attendanceHistoryListAdapter = new AttendanceHistoryListAdapter(mContext, attendanceList);
        rcvAttendanceHistory.setAdapter(attendanceHistoryListAdapter);
        attendanceHistoryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckInStatusChange() {
        ((BaseActivity) mContext).getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }
}
