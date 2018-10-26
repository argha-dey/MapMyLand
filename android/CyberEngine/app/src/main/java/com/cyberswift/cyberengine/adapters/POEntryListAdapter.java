package com.cyberswift.cyberengine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.fragments.POAddEditLogFragment;
import com.cyberswift.cyberengine.models.POWorkingDaysList;

import java.util.ArrayList;

public class POEntryListAdapter extends RecyclerView.Adapter<POEntryListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<POWorkingDaysList> arrList;
    private POWorkingDaysList sModel = new POWorkingDaysList();

    public POEntryListAdapter(Context context, ArrayList<POWorkingDaysList> arrList_WorkingDays) {
        this.context = context;
        this.arrList = arrList_WorkingDays;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_po_entry_list, parent, false);
        return new POEntryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        sModel = arrList.get(position);
        holder.tvDate.setText(sModel.getLogDate());
        holder.tvDay.setText(sModel.getDay());

        float hours = 0;
        String logDetails = "";

        // set logged hours
        hours = Float.parseFloat(sModel.getTotalHours());
        if (hours > 1)
            logDetails = "Total logged " + sModel.getTotalHours() + " hours";
        else
            logDetails = "Total logged " + sModel.getTotalHours() + " hour";
        holder.tvLoggedHours.setText(logDetails);

        // set billed hours
        hours = Float.parseFloat(sModel.getBilledHours());
        if (hours > 1)
            logDetails = "Total billed " + sModel.getBilledHours() + " hours";
        else
            logDetails = "Total billed " + sModel.getBilledHours() + " hour";
        holder.tvBillingHours.setText(logDetails);

        holder.layoutRowPOEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new POAddEditLogFragment(context, arrList.get(position).getLogDate(), arrList.get(position).getDay());
                FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_container, newFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate, tvDay, tvLoggedHours, tvBillingHours;
        private LinearLayout layoutRowPOEntry;
        MyViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvLoggedHours = itemView.findViewById(R.id.tvLoggedHours);
            tvBillingHours = itemView.findViewById(R.id.tvBillingHours);
            layoutRowPOEntry = itemView.findViewById(R.id.layoutRowPOEntry);
        }

    }


}
