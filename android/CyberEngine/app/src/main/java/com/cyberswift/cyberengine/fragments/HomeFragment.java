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
import android.widget.Button;
import android.widget.Toast;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.activities.BaseActivity;
import com.cyberswift.cyberengine.adapters.CheckInCheckOutTimeListAdapter;
import com.cyberswift.cyberengine.listeners.CheckInCheckOutCallBack;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.services.VolleyTaskManager;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.Util;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static com.cyberswift.cyberengine.utility.Util.checkAndRequestAllPermissions;

@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment implements CheckInCheckOutCallBack {

    private Context mContext;
    private VolleyTaskManager volleyTaskManager;
    private Prefs mPrefs;
    private RecyclerView rcvCheckinCheckoutTime;
    private CheckInCheckOutTimeListAdapter checkInCheckOutTimeListAdapter;
    private Button btn_punchin, btn_punchout;


    public HomeFragment(Context mContext) {
        this.mContext = mContext;
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
        initView(v);

        return v;
    }


    private void initView(View v) {
        rcvCheckinCheckoutTime = v.findViewById(R.id.rcvCheckinCheckoutTime);
        rcvCheckinCheckoutTime.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        btn_punchin = v.findViewById(R.id.btn_punchin);
        btn_punchout = v.findViewById(R.id.btn_punchout);

        setCheckInCheckOutButtons();
        btn_punchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).prepareCheckInCheckOutProcedure(true);
            }
        });

        btn_punchout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).prepareCheckInCheckOutProcedure(false);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkAndRequestAllPermissions(mContext)) {
            if (Util.checkConnectivity(mContext)) {
                callCheckInCheckOutList();
            }
        }
    }


    private void callCheckInCheckOutList() {
        SimpleDateFormat gmtDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_ID, Util.getMobileDeviceID(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_SIM_ID_LIST, Util.getMobileSimIdArr(mContext));
        paramsMap.put(WebServiceConstants.REQ_PARAM_DEVICE_TYPE, AppConstants.DEVICE_TYPE);
        volleyTaskManager.getCurrentDateAttendanceList(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                    JSONArray timeList = resultJsonObject.optJSONArray(WebServiceConstants.RES_PARAM_RESULT);
                    if (timeList != null && timeList.length() > 0) {
                        setTimeListAdapter(timeList);
                    }
                } else {
                    if (resultJsonObject.optInt(WebServiceConstants.RES_PARAM_ERROR_CODE) == AppConstants.ERROR_CODE_AUTHENTICATION_FAILURE) {
                        ((BaseActivity) getActivity()).logout();
                        return;
                    }
                    String msg = "Unexpected error. Please try again.";
                    if (resultJsonObject.opt(WebServiceConstants.RES_PARAM_ERROR_MESSAGE) != null) {
                        msg = resultJsonObject.optString(WebServiceConstants.RES_PARAM_ERROR_MESSAGE);
                    }

                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {}
        });
    }


    private void setTimeListAdapter(JSONArray timeList) {
        checkInCheckOutTimeListAdapter = new CheckInCheckOutTimeListAdapter(mContext, timeList);
        rcvCheckinCheckoutTime.setAdapter(checkInCheckOutTimeListAdapter);
        checkInCheckOutTimeListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCheckInStatusChange() {
        ((BaseActivity) mContext).getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }


    private void setCheckInCheckOutButtons() {
        if (mPrefs.getCheckInStatus()) {
            btn_punchin.setBackground(getResources().getDrawable(R.drawable.rect_withborder_accent));
            btn_punchin.setEnabled(false);
            btn_punchout.setBackground(getResources().getDrawable(R.drawable.rect_withborder_primary));
            btn_punchout.setEnabled(true);
        } else {
            btn_punchout.setBackground(getResources().getDrawable(R.drawable.rect_withborder_accent));
            btn_punchout.setEnabled(false);
            btn_punchin.setBackground(getResources().getDrawable(R.drawable.rect_withborder_primary));
            btn_punchin.setEnabled(true);
        }
    }
}
