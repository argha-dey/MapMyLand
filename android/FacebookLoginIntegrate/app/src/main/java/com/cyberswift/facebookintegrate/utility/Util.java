package com.cyberswift.facebookintegrate.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.cyberswift.facebookintegrate.model.SaveListModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User-129-pc on 24-10-2018.
 */

public class Util {

    private static String SAVEARRAYLIST = "SAVEARRAYLIST";
    public static final String PREF_NAME_REFRESH = "SHAPE pref";
    // Saving Accepted Proposal details
    public static void saveArrayList(final Context mContext, ArrayList<SaveListModel> saveListModels) {
        SharedPreferences savePrefs = mContext.getSharedPreferences(PREF_NAME_REFRESH, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = savePrefs.edit();
        try {
            prefsEditor.putString(SAVEARRAYLIST, ObjectSerializer.serialize(saveListModels));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    // Fetching Pending Proposal details
    public static ArrayList<SaveListModel> fetcharraylist(final Context mContext) {
        SharedPreferences savePrefs = mContext.getSharedPreferences(PREF_NAME_REFRESH, Context.MODE_PRIVATE);
        ArrayList<SaveListModel> arrayList = null;
        String serializeOrg = savePrefs.getString(SAVEARRAYLIST, null);
        try {
            if (serializeOrg != null) {
                arrayList = (ArrayList<SaveListModel>) ObjectSerializer.deserialize(serializeOrg);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}
