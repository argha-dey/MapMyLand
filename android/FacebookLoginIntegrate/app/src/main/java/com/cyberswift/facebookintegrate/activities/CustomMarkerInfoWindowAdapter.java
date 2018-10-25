package com.cyberswift.facebookintegrate.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cyberswift.facebookintegrate.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by User-129-pc on 04-10-2018.
 */

public class CustomMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public CustomMarkerInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.custom_marker_view, null);
      //  LatLng latLng = arg0.getPosition();
        EditText et_add_text =  v.findViewById(R.id.et_add_text);
        return v;
    }
}
