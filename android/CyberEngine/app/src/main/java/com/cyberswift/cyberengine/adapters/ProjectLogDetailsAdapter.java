package com.cyberswift.cyberengine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.models.ProjectLogDetails;

import java.util.ArrayList;

public class ProjectLogDetailsAdapter extends RecyclerView.Adapter<ProjectLogDetailsAdapter.MyViewHolder> {

    private Context context;
    private ProjectLogDetails sModel = new ProjectLogDetails();
    private ArrayList<ProjectLogDetails> arrList;

    public ProjectLogDetailsAdapter(Context context, ArrayList<ProjectLogDetails> arrList) {
        this.context = context;
        this.arrList = arrList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_log_details, parent, false);
        return new ProjectLogDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        sModel = arrList.get(position);
        holder.tvProjectName.setText(sModel.getProjectName());

        SubProjectLogDetailsAdapter sAdapter = new SubProjectLogDetailsAdapter(context, sModel.getLogSubDetails());
        holder.rvSubProjectLogDetails.setAdapter(sAdapter);
    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProjectName;
        private RecyclerView rvSubProjectLogDetails;
        MyViewHolder(View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            rvSubProjectLogDetails = itemView.findViewById(R.id.rvSubProjectLogDetails);
            rvSubProjectLogDetails.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            rvSubProjectLogDetails.setItemAnimator(new DefaultItemAnimator());
        }

    }


}
