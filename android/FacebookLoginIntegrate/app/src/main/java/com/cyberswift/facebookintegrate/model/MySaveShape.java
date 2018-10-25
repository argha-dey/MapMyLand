package com.cyberswift.facebookintegrate.model;


import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by User-129-pc on 11-10-2018.
 */

public class MySaveShape extends RealmObject implements Serializable {

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public RealmList<MySingleShape> getMySingleShapes() {
        return mySingleShapes;
    }

    public void setMySingleShapes(RealmList<MySingleShape> mySingleShapes) {
        this.mySingleShapes = mySingleShapes;
    }

    private String fileName = "";
    private RealmList<MySingleShape> mySingleShapes = new RealmList<MySingleShape>();

}
