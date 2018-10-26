package com.cyberswift.cyberengine.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.activities.BaseActivity;
import com.cyberswift.cyberengine.adapters.POEntryListAdapter;
import com.cyberswift.cyberengine.listeners.CheckInCheckOutCallBack;
import com.cyberswift.cyberengine.listeners.ServerResponseCallback;
import com.cyberswift.cyberengine.models.POWorkingDaysList;
import com.cyberswift.cyberengine.services.VolleyTaskManager;
import com.cyberswift.cyberengine.utility.AppConstants;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.WebServiceConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class POEntryListFragment extends BaseFragment implements CheckInCheckOutCallBack {

    private Context mContext;
    private VolleyTaskManager volleyTaskManager;
    private Prefs mPrefs;
    private ImageView iv_no_data_found;
    private RecyclerView rv_po_entry_list;
    private POEntryListAdapter pOEntryListAdapter;


    public POEntryListFragment(Context mContext) {
        this.mContext = mContext;
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_po_entry_list, container, false);
        mContext = getActivity();
        volleyTaskManager = new VolleyTaskManager(mContext);
        mPrefs = new Prefs(mContext);
        initView(v);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        fetchWorkingLogDays();
    }


    private void initView(View v) {
        iv_no_data_found = v.findViewById(R.id.iv_no_data_found);
        rv_po_entry_list = v.findViewById(R.id.rv_po_entry_list);

        rv_po_entry_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rv_po_entry_list.setItemAnimator(new DefaultItemAnimator());
    }

    private void fetchWorkingLogDays() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(WebServiceConstants.PARAM_USER_ID, mPrefs.getUserId());

        volleyTaskManager.getWorkingLogDays(new JSONObject(paramsMap), true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                if (resultJsonObject.optBoolean(WebServiceConstants.RES_PARAM_STATUS)) {
                    ArrayList<POWorkingDaysList> arrList_WorkingDays = new ArrayList<>();
                    JSONArray jArr_DaysList = resultJsonObject.optJSONObject(WebServiceConstants.RES_PARAM_RESULT).optJSONArray("Weeks");
                    if (jArr_DaysList != null && jArr_DaysList.length() > 0) {
                        for (int i = 0; i < jArr_DaysList.length(); i++) {
                            JSONObject jObj = jArr_DaysList.optJSONObject(i);
                            JSONArray jArr = jObj.optJSONArray("log_hour_sums");
                            for (int j = 0; j < jArr.length(); j++) {
                                POWorkingDaysList sModel = new POWorkingDaysList();
                                sModel.setCreatedById(jArr.optJSONObject(j).optString("created_by_id"));
                                sModel.setDay(jArr.optJSONObject(j).optString("day"));
                                sModel.setIsAbsent(jArr.optJSONObject(j).optString("is_absent"));
                                sModel.setLogDate(jArr.optJSONObject(j).optString("log_date"));
                                sModel.setBilledHours(jArr.optJSONObject(j).optString("sum_billhours"));
                                sModel.setTotalHours(jArr.optJSONObject(j).optString("sum_hours"));
                                sModel.setWeekNo(jObj.optInt("week_no"));
                                arrList_WorkingDays.add(sModel);
                            }
                        }

                        iv_no_data_found.setVisibility(View.GONE);
                        setAttendanceListAdapter(arrList_WorkingDays);
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

    private void setAttendanceListAdapter(ArrayList<POWorkingDaysList> arrList_WorkingDays) {
        pOEntryListAdapter = new POEntryListAdapter(mContext, arrList_WorkingDays);
        rv_po_entry_list.setAdapter(pOEntryListAdapter);
        pOEntryListAdapter.notifyDataSetChanged();
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
