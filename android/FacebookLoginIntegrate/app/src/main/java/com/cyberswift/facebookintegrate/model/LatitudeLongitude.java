package com.cyberswift.facebookintegrate.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by User-129-pc on 11-10-2018.
 */

public class LatitudeLongitude extends RealmObject implements Serializable{
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
