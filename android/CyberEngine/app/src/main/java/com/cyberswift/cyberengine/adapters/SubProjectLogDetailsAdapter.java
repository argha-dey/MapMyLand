package com.cyberswift.cyberengine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.models.SubProjectLogDetails;

import java.util.ArrayList;

public class SubProjectLogDetailsAdapter extends RecyclerView.Adapter<SubProjectLogDetailsAdapter.MyViewHolder> {

    private Context context;
    private SubProjectLogDetails sModel = new SubProjectLogDetails();
    private ArrayList<SubProjectLogDetails> arrList;

    public SubProjectLogDetailsAdapter(Context context, ArrayList<SubProjectLogDetails> arrList) {
        this.context = context;
        this.arrList = arrList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subproject_list, parent, false);
        return new SubProjectLogDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        sModel = arrList.get(position);
        holder.tvSubProjectName.setText(sModel.getSubProjectName());
        holder.etTotalLoggedHour.setText(sModel.getTotalLoggedHours());
        holder.etBilledHour.setText(sModel.getBillableHours());
        holder.et_log_desc.setText(sModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSubProjectName;
        private EditText etTotalLoggedHour, etBilledHour, et_log_desc;
        MyViewHolder(View itemView) {
            super(itemView);
            tvSubProjectName = itemView.findViewById(R.id.tvSubProjectName);
            etTotalLoggedHour = itemView.findViewById(R.id.etTotalLoggedHour);
            etBilledHour = itemView.findViewById(R.id.etBilledHour);
            et_log_desc = itemView.findViewById(R.id.et_log_desc);
        }

    }


}
