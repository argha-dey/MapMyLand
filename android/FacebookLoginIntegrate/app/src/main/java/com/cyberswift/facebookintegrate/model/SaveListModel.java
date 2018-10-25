package com.cyberswift.facebookintegrate.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User-129-pc on 24-10-2018.
 */

public class SaveListModel implements Serializable{

    String shapeType;
    int shapId;
    ArrayList<LatitudeLongitude> latitudeLongitudes=new ArrayList<>();

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public int getShapId() {
        return shapId;
    }

    public void setShapId(int shapId) {
        this.shapId = shapId;
    }

    public ArrayList<LatitudeLongitude> getLatitudeLongitudes() {
        return latitudeLongitudes;
    }

    public void setLatitudeLongitudes(ArrayList<LatitudeLongitude> latitudeLongitudes) {
        this.latitudeLongitudes = latitudeLongitudes;
    }

}
