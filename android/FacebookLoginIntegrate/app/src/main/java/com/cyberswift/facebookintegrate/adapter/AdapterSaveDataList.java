
package com.cyberswift.facebookintegrate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberswift.facebookintegrate.R;
import com.cyberswift.facebookintegrate.activities.MultiShapeMapActivity;
import com.cyberswift.facebookintegrate.model.LatitudeLongitude;
import com.cyberswift.facebookintegrate.model.MySaveShape;
import com.cyberswift.facebookintegrate.model.SaveListModel;
import com.cyberswift.facebookintegrate.utility.Util;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import io.realm.RealmList;


public class AdapterSaveDataList extends RecyclerView.Adapter<AdapterSaveDataList.MyViewHolder> implements OnMapReadyCallback {
    Context mContext;
    RecyclerView rcl;
    GoogleMap mMap;
    private RealmList<MySaveShape> saveShapeList = new RealmList<>();

    public AdapterSaveDataList(Context _mContext, RecyclerView rcv) {
        this.mContext = _mContext;
        this.rcl = rcv;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shape_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        // holder.tv_title.setText(saveShapeList.get(position).getTitle());

        holder.tv_title.setText(saveShapeList.get(position).getFileName());
        holder.rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<SaveListModel> saveListModelsArray = new ArrayList<>();
                for (int i = 0; i < saveShapeList.get(position).getMySingleShapes().size(); i++) {
                    SaveListModel saveListModelObject = new SaveListModel();
                    saveListModelObject.setShapeType(saveShapeList.get(position).getMySingleShapes().get(i).getShapeName());
                    saveListModelObject.setShapId(saveShapeList.get(position).getMySingleShapes().get(i).getShapeId());
                    ArrayList<LatitudeLongitude> latitudeLongitudesArray = new ArrayList<>();
                    for (int j = 0; j < saveShapeList.get(position).getMySingleShapes().get(i).getLatitudeLongitudes().size(); j++) {
                        LatitudeLongitude latitudeLongitude = new LatitudeLongitude();
                        latitudeLongitude.setLongitude(saveShapeList.get(position).getMySingleShapes().get(i).getLatitudeLongitudes().get(j).getLongitude());
                        latitudeLongitude.setLatitude(saveShapeList.get(position).getMySingleShapes().get(i).getLatitudeLongitudes().get(j).getLatitude());
                        latitudeLongitudesArray.add(latitudeLongitude);
                    }
                    saveListModelObject.setLatitudeLongitudes(latitudeLongitudesArray);
                    saveListModelsArray.add(saveListModelObject);
                }


                Toast.makeText(mContext,
                        "shape List Array : " + saveListModelsArray.size(), Toast.LENGTH_SHORT).show();

// save data i arrayList...
                Util.saveArrayList(mContext, saveListModelsArray);


                Intent intent = new Intent(mContext, MultiShapeMapActivity.class);
                intent.putExtra("SAVE_NAME_AS_FILE", saveShapeList.get(position).getFileName());
                Activity activity = (Activity) mContext;
                mContext.startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return saveShapeList.size();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public RelativeLayout rl_title;
        public ImageView iv_arrow;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            rl_title = (RelativeLayout) itemView.findViewById(R.id.rl_title);
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);

        }
    }

    public void AddShapeList(RealmList<MySaveShape> _arrListOfData) {
        saveShapeList.clear();
        if (_arrListOfData != null
                && _arrListOfData.size() > 0) {
            saveShapeList.addAll(_arrListOfData);
        }
        notifyDataSetChanged();

    }


}
