package com.cyberswift.cyberengine.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyberswift.cyberengine.R;

import org.json.JSONArray;

public class CheckInCheckOutTimeListAdapter extends RecyclerView.Adapter<CheckInCheckOutTimeListAdapter.MyViewHolder> {

    private Context context;
    private JSONArray timeList;


    public CheckInCheckOutTimeListAdapter(Context context, JSONArray timeList) {
        this.context = context;
        this.timeList = timeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_time_list_row, parent, false);
        return new CheckInCheckOutTimeListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String timeDetails = timeList.opt(position).toString();
        if (timeDetails != null && timeDetails.length() > 0) {
            holder.tvTimeDetails.setText(timeDetails);
            if (position == 0 || position == timeList.length()-1) {
                holder.ivClock.setImageResource(R.drawable.round_clock_red);
                holder.tvTimeDetails.setTextColor(context.getResources().getColor(R.color.colorAccentRed));
            }
        }
    }

    @Override
    public int getItemCount() {
        return timeList.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivClock;
        private TextView tvTimeDetails;

        MyViewHolder(View itemView) {
            super(itemView);

            ivClock = itemView.findViewById(R.id.ivClock);
            tvTimeDetails = itemView.findViewById(R.id.tvTimeDetails);
        }
    }
}
