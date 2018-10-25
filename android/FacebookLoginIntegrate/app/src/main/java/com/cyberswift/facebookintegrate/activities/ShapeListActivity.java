package com.cyberswift.facebookintegrate.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cyberswift.facebookintegrate.R;
import com.cyberswift.facebookintegrate.adapter.AdapterSaveDataList;
import com.cyberswift.facebookintegrate.model.MySaveShape;
import com.cyberswift.facebookintegrate.model.SaveListModel;
import com.cyberswift.facebookintegrate.utility.Util;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by User-129-pc on 23-10-2018.
 */

public class ShapeListActivity extends AppCompatActivity {
    private Context mContext;
    RecyclerView rv_shape_list;
    ImageView add_fab;
    Realm realm;
    private AdapterSaveDataList adapterSaveDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_list_activity_layout);
        mContext = ShapeListActivity.this;
        initView();
        realmDataBaseInitConfig();
        setDataRecyclerView();
    }

    private void realmDataBaseInitConfig() {
        // Realm database Configuration ...
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }


    private void initView() {
        rv_shape_list = findViewById(R.id.rv_shape_list);
        add_fab = findViewById(R.id.add_fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.saveArrayList(mContext,new ArrayList<SaveListModel>());
                Intent intent = new Intent(mContext, MultiShapeMapActivity.class);
                Activity activity = (Activity) mContext;
                mContext.startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
            }
        }
        );

    }

    private void setDataRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rv_shape_list.setLayoutManager(mLayoutManager);
        adapterSaveDataList = new AdapterSaveDataList(mContext, rv_shape_list);

        if (realm == null || realm.isClosed()) {
            realm = Realm.getDefaultInstance();
            RealmResults<MySaveShape> results = realm.where(MySaveShape.class).findAllAsync();
            results.load();

            RealmList<MySaveShape> tempList = new RealmList<>();
            for (MySaveShape mySaveShape : results) {
                tempList.add(mySaveShape);
            }
            adapterSaveDataList.AddShapeList(tempList);
            rv_shape_list.setAdapter(adapterSaveDataList);
        }
    }

    @Override
    public void onBackPressed() {
        // do code hare....

    }
}
