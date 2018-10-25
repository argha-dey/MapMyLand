package com.cyberswift.facebookintegrate.activities;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by User-129-pc on 08-10-2018.
 */


public class MapShape  {
    public String type;
  public int shape_id;
    PolygonOptions polygonOptions;
    PolygonOptions sqPolygonOptions;
    PolylineOptions polylineOptions;
    CircleOptions circleOptions;
    ArrayList<Marker> arrayOfPolyLineMarker;

    private Polyline polyline;
    private Polygon polygon;
    private Polygon mSqPolygon;
    private Circle mCircle;

    public MapShape(String _type, int shape_id, ArrayList<Marker> _arrayOfMarker) {
        this.type = _type;
        this.shape_id = shape_id;
        this.arrayOfPolyLineMarker = _arrayOfMarker;
    }



    public void drawShape(GoogleMap mMap) {

        if (this.type.equals("polyline")) {
            if (polyline != null) {
                polyline.remove();
            }
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED).geodesic(true);
            polylineOptions.width(3);
            for (int i = 0; i < arrayOfPolyLineMarker.size(); i++) {
                polylineOptions.add(arrayOfPolyLineMarker.get(i).getPosition());
            }
            polyline = mMap.addPolyline(polylineOptions);

        } else if (this.type.equals("polygon")) {

            if (polygon != null) {
                polygon.remove();
            }
            polygonOptions = new PolygonOptions()
                    .strokeWidth(3)
                    .fillColor(Color.RED)
                    .strokeColor(Color.BLUE).geodesic(true);

            for (int i = 0; i < arrayOfPolyLineMarker.size(); i++) {
                polygonOptions.add(arrayOfPolyLineMarker.get(i).getPosition());
            }
            polygon = mMap.addPolygon(polygonOptions);
        } else if (this.type.equals("square")) {

            if (mSqPolygon != null) {
                mSqPolygon.remove();
            }
            sqPolygonOptions = new PolygonOptions()
                    .strokeWidth(3)
                    .fillColor(Color.RED)
                    .strokeColor(Color.BLUE).geodesic(true);

            for (int i = 0; i < arrayOfPolyLineMarker.size(); i++) {
                sqPolygonOptions.add(arrayOfPolyLineMarker.get(i).getPosition());
            }

            mSqPolygon = mMap.addPolygon(sqPolygonOptions);

        } else if (this.type.equals("circle")) {
            if (mCircle != null) {
                mCircle.remove();
            }

            Location myLocation = new Location("");
            Location dest_location = new Location("");
            myLocation.setLatitude(arrayOfPolyLineMarker.get(0).getPosition().latitude);
            myLocation.setLongitude(arrayOfPolyLineMarker.get(0).getPosition().longitude);
            dest_location.setLatitude(arrayOfPolyLineMarker.get(1).getPosition().latitude);
            dest_location.setLongitude(arrayOfPolyLineMarker.get(1).getPosition().longitude);
            double distance = myLocation.distanceTo(dest_location);
            circleOptions = new CircleOptions()
                    .center(new LatLng(arrayOfPolyLineMarker.get(0).getPosition().latitude, arrayOfPolyLineMarker.get(0).getPosition().longitude))
                    .radius(distance)
                    .fillColor(Color.RED)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(3);
            mCircle = mMap.addCircle(circleOptions);
        }
    }


    public int getShape_id() {
        return shape_id;
    }

    public String getShape_type() {
        return type;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }


}
