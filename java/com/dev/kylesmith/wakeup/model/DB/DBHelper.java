package com.dev.kylesmith.wakeup.model.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kylesmith on 1/8/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Main.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " FLOAT";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String VARCHAR_TYPE = " VARCHAR(255)";
    private static final String SMALL_VARCHAR_TYPE = " VARCHAR(9)";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.Appointments.TABLE_NAME + " (" +
                    DBContract.Appointments._ID + " INTEGER PRIMARY KEY," +
                    DBContract.Appointments.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    DBContract.Appointments.COLUMN_NAME_TIME + DATETIME_TYPE + COMMA_SEP +
                    DBContract.Appointments.COLUMN_NAME_LONGITUDE + VARCHAR_TYPE + COMMA_SEP +
                    DBContract.Appointments.COLUMN_NAME_LATITUDE + VARCHAR_TYPE + COMMA_SEP +
                    DBContract.Appointments.COLUMN_NAME_LOC_AREA + FLOAT_TYPE + COMMA_SEP +
                    DBContract.Appointments.COLUMN_NAME_ZOOM_LEVEL + FLOAT_TYPE + COMMA_SEP +
                    DBContract.Appointments.COLUMN_NAME_LINEKEY + VARCHAR_TYPE + COMMA_SEP +
                    DBContract.Appointments.COLUMN_NAME_CREATED + DATETIME_TYPE +
            " )";
    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + DBContract.User.TABLE_NAME + " (" +
                    DBContract.User._ID + "INTEGER PRIMARY KEY," +
                    DBContract.User.COLUMN_NAME_PHONE_NUM + VARCHAR_TYPE + COMMA_SEP +
                    DBContract.User.COLUMN_NAME_USER_ID + SMALL_VARCHAR_TYPE + COMMA_SEP +
                    DBContract.User.COLUMN_NAME_PHOTO_LOCATION + VARCHAR_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.Appointments.TABLE_NAME;
    private static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.User.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_USER_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
