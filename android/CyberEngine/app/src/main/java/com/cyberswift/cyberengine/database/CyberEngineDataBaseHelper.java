package com.cyberswift.cyberengine.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyberswift.cyberengine.models.AttendanceUserClass;
import com.cyberswift.cyberengine.utility.Constants;

import java.util.ArrayList;


/**
 * Created by User-129-pc on 25-07-2018.
 */

public class CyberEngineDataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;
    /**
     * Table Creation
     **/
    private static final String CREATE_AUTO_ATTENDANCE_TABLE = "CREATE TABLE " + Constants.AUTO_ATTENDANCE_TABLE + "("
            + Constants.DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"// Primary key
            + Constants.DB_USER_ID + " INTEGER,"
            + Constants.DB_FUSE_LAT + " TEXT,"
            + Constants.DB_FUSE_LON + " TEXT,"
            + Constants.DB_DATE_TIME + " TEXT,"
            + Constants.DB_UPDATE_FLAG + " INTEGER,"
            + Constants.DB_MOBILE_STATUS + " TEXT"
            + ")";

    /**  This query was used to update the Database from version 1 to version 2. This is not needed anymore, but the query is being kept for reference. **/
    //private static final String ALTER_AUTO_ATTENDANCE_TABLE1 = "ALTER TABLE " + AUTO_ATTENDANCE_TABLE + " ADD COLUMN " + Constants.DB_MOBILE_STATUS + " TEXT";

    private static final String SELECT_USER_VAL = Constants.DB_ID + ", " + Constants.USER_ID + ", " + Constants.DB_FUSE_LAT + ", " + Constants.DB_FUSE_LON + ", " +
            Constants.DB_DATE_TIME + ", " + Constants.DB_UPDATE_FLAG + ", " + Constants.DB_MOBILE_STATUS;


    public CyberEngineDataBaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * table created here by SQLiteDatabase
     **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AUTO_ATTENDANCE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("PRAGMA user_version = " + newVersion);
            try {
                /**  This query was used to update the Database from version 1 to version 2. This is not needed anymore, but the query is being kept for reference. **/
                //db.execSQL(ALTER_AUTO_ATTENDANCE_TABLE1);

                updateDatabase(db);
            } catch (Exception ignored) {}
        }
    }

    /**  This query is used to update the Database from version 2 to version 3. Here the column USER_ID is being changed to type INTEGER from type TEXT. **/
    private void updateDatabase(SQLiteDatabase db) {
        String CREATE_TEMP_AUTO_ATTENDANCE_TABLE = "CREATE TABLE " + "TEMP_AUTO_ATTENDANCE_TABLE" + "("
                + Constants.DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"// Primary key
                + Constants.DB_USER_ID + " INTEGER,"
                + Constants.DB_FUSE_LAT + " TEXT,"
                + Constants.DB_FUSE_LON + " TEXT,"
                + Constants.DB_DATE_TIME + " TEXT,"
                + Constants.DB_UPDATE_FLAG + " INTEGER,"
                + Constants.DB_MOBILE_STATUS + " TEXT"
                + ")";
        String COPY_VALUES_TO_TEMP_AUTO_ATTENDANCE_TABLE = "INSERT INTO "+ "TEMP_AUTO_ATTENDANCE_TABLE" +" SELECT " + Constants.DB_ID +
                ", CAST(" + Constants.DB_USER_ID + " AS INTEGER)," + Constants.DB_FUSE_LAT + ", " + Constants.DB_FUSE_LON + ", " + Constants.DB_DATE_TIME +
                ", " + Constants.DB_UPDATE_FLAG + ", " + Constants.DB_MOBILE_STATUS + "FROM " + Constants.AUTO_ATTENDANCE_TABLE;
        String DROP_EXISTING_AUTO_ATTENDANCE_TABLE = "DROP TABLE " + Constants.AUTO_ATTENDANCE_TABLE;
        String RENAME_TEMP_AUTO_ATTENDANCE_TABLE = "ALTER TABLE " + "TEMP_AUTO_ATTENDANCE_TABLE" + " RENAME TO " + Constants.AUTO_ATTENDANCE_TABLE;

        db.execSQL(CREATE_TEMP_AUTO_ATTENDANCE_TABLE);
        db.execSQL(COPY_VALUES_TO_TEMP_AUTO_ATTENDANCE_TABLE);
        db.execSQL(DROP_EXISTING_AUTO_ATTENDANCE_TABLE);
        db.execSQL(RENAME_TEMP_AUTO_ATTENDANCE_TABLE);
    }

    /**
     * Insert data of attendance
     **/
    public synchronized void addAttendanceInfo(AttendanceUserClass attendanceUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Constants.USER_ID, attendanceUser.getUser_id());
        content.put(Constants.DB_FUSE_LAT, attendanceUser.getLat());
        content.put(Constants.DB_FUSE_LON, attendanceUser.getLon());
        content.put(Constants.DB_DATE_TIME, attendanceUser.getDate_time());
        content.put(Constants.DB_UPDATE_FLAG, attendanceUser.getUpdate_flag());
        content.put(Constants.DB_MOBILE_STATUS, attendanceUser.getMobileStatus());
        db.insert(Constants.AUTO_ATTENDANCE_TABLE, null, content);
        db.close(); //Close the database Connection
    }


    // Deleting all attendance
    public synchronized void deleteAllAttendance(int newOrOld) {
        String selectQuery = "";
        selectQuery = "SELECT* " + " FROM " + Constants.AUTO_ATTENDANCE_TABLE + " WHERE " + Constants.DB_UPDATE_FLAG + "=" + newOrOld;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                db.execSQL("delete from " + Constants.AUTO_ATTENDANCE_TABLE);
                db.close();
            }
        }
    }


    // Getting ready to upload in background
    public synchronized ArrayList<AttendanceUserClass> getUserReadyToUpload(int newOrOld) {
        ArrayList<AttendanceUserClass> attendanceUserDB = new ArrayList<>();
        // Select All Query
        String selectQuery = "";

        selectQuery = "SELECT " + SELECT_USER_VAL + " FROM " + Constants.AUTO_ATTENDANCE_TABLE
                + " WHERE " + Constants.DB_UPDATE_FLAG + "=" + newOrOld + ";";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;
        if (cursor != null) {
            // Looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    if (i == 15) {
                        break;
                    }
                    attendanceUserDB.add(new AttendanceUserClass(cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            cursor.getString(6)));
                    i++;
                } while (cursor.moveToNext());
            } else {
                attendanceUserDB = null;
            }
        } else {
            attendanceUserDB = null;
        }
        // close inserting data from database
        db.close();
        // return contact list
        return attendanceUserDB;
    }


    // Updating single row...
    public synchronized int updateAttendanceTableBackgroundServerResponse(int updateStatus, int localId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.DB_UPDATE_FLAG, updateStatus);
        // updating row
        return db.update(Constants.AUTO_ATTENDANCE_TABLE, values, Constants.DB_ID + " = ?",
                new String[]{String.valueOf(localId)});
    }
}

