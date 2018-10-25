package com.cyberswift.facebookintegrate.model;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by User-129-pc on 11-10-2018.
 */

public class MySingleShape extends RealmObject implements Serializable {
    private int shapeId;

    public int getShapeId() {
        return shapeId;
    }

    public void setShapeId(int shapeId) {
        this.shapeId = shapeId;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public RealmList<LatitudeLongitude> getLatitudeLongitudes() {
        return latitudeLongitudes;
    }

    public void setLatitudeLongitudes(RealmList<LatitudeLongitude> latitudeLongitudes) {
        this.latitudeLongitudes = latitudeLongitudes;
    }

    private String shapeName;
    private RealmList<LatitudeLongitude> latitudeLongitudes = new RealmList<LatitudeLongitude>();


}
