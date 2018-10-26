package com.cyberswift.cyberengine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyberswift.cyberengine.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class AttendanceHistoryListAdapter extends RecyclerView.Adapter<AttendanceHistoryListAdapter.MyViewHolder> {

    private Context context;
    private JSONArray attendanceHistoryList;


    public AttendanceHistoryListAdapter(Context context, JSONArray timeList) {
        this.context = context;
        this.attendanceHistoryList = timeList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_attendance_history_time_list_row, parent, false);
        return new AttendanceHistoryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        JSONObject dateData = attendanceHistoryList.optJSONObject(position);
        String date = dateData.optString("date");
        holder.tvDate.setText(date);
        String inTime = dateData.optString("intime");
        holder.tvCheckInTime.setText("You checked in at " + inTime);
        String outTime = dateData.optString("outtime");
        if (outTime.equalsIgnoreCase("auto"))
            holder.tvCheckOutTime.setText("You were forced checked out at 11:59 PM.");
        else
            holder.tvCheckOutTime.setText("You checked out at " + outTime);
    }

    @Override
    public int getItemCount() {
        return attendanceHistoryList.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private TextView tvCheckInTime;
        private TextView tvCheckOutTime;

        MyViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvCheckInTime = itemView.findViewById(R.id.tvCheckInTime);
            tvCheckOutTime = itemView.findViewById(R.id.tvCheckOutTime);
        }
    }
}
