package com.dev.kylesmith.wakeup.model.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.net.Uri;
import android.util.Log;

import com.dev.kylesmith.wakeup.model.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kylesmith on 1/8/15.
 *
 * Used for all database commands
 */
public class DBAccessor {
    private Context context;
    private DBHelper dbHelper;
    private static final String TAG = "DBAccessor";

    public DBAccessor(Context context){
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    // Insert a new appointment
    public long insertAppointment(String title,
                                  String longitude,
                                  String latitude,
                                  float loc_area,
                                  float zoom_level,
                                  Date time,
                                  String linekey){
        // Map all values
        ContentValues values = new ContentValues();
        values.put(DBContract.Appointments.COLUMN_NAME_TITLE, title);
        values.put(DBContract.Appointments.COLUMN_NAME_LATITUDE, latitude);
        values.put(DBContract.Appointments.COLUMN_NAME_LONGITUDE, longitude);
        values.put(DBContract.Appointments.COLUMN_NAME_LOC_AREA, loc_area);
        values.put(DBContract.Appointments.COLUMN_NAME_ZOOM_LEVEL, zoom_level);
        Date date = new Date();
        values.put(DBContract.Appointments.COLUMN_NAME_CREATED, date.toString());
        values.put(DBContract.Appointments.COLUMN_NAME_TIME, time.toString());
        values.put(DBContract.Appointments.COLUMN_NAME_LINEKEY, linekey);

        // Insert values into database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = db.insert(DBContract.Appointments.TABLE_NAME, DBContract.Appointments.COLUMN_NAME_NULLABLE, values);

        // Log the new id
        Log.i(TAG, "New id: " + newId);

        return newId;
    }






    public long insertUserInfo(int userId, String phone_num, Uri photo_loc){
        ContentValues values = new ContentValues();
        values.put(DBContract.User.COLUMN_NAME_PHONE_NUM, phone_num);
        values.put(DBContract.User.COLUMN_NAME_PHOTO_LOCATION, photo_loc.toString());
        values.put(DBContract.User.COLUMN_NAME_USER_ID, Integer.toString(userId));

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = db.insert(DBContract.User.TABLE_NAME, DBContract.User.COLUMN_NAME_NULLABLE, values);
        return newId;
    }



    public HashMap<String, Object> getUserInfo(){
        HashMap userInfo = new HashMap<String, Object>();
        String[] projection = {
                DBContract.User.COLUMN_NAME_PHOTO_LOCATION,
                DBContract.User.COLUMN_NAME_USER_ID,
                DBContract.User.COLUMN_NAME_PHONE_NUM
        };

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(DBContract.User.TABLE_NAME, projection, null, null, null, null, null);
        c.moveToFirst();
        userInfo.put(Constants.MAPKEY_PHONE_NUM, c.getString(c.getColumnIndexOrThrow(DBContract.User.COLUMN_NAME_PHONE_NUM)));
        userInfo.put(Constants.MAPKEY_PHOTO_URI, c.getString(c.getColumnIndexOrThrow(DBContract.User.COLUMN_NAME_PHOTO_LOCATION)));
        userInfo.put(Constants.MAPKEY_USER_ID, c.getString(c.getColumnIndexOrThrow(DBContract.User.COLUMN_NAME_USER_ID)));

        return userInfo;
    }



    public void deleteAppointment(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DBContract.Appointments._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(DBContract.Appointments.TABLE_NAME, selection, selectionArgs);
    }







    public Cursor getAppointment(long id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DBContract.Appointments._ID,
                DBContract.Appointments.COLUMN_NAME_TITLE,
                DBContract.Appointments.COLUMN_NAME_CREATED,
                DBContract.Appointments.COLUMN_NAME_LONGITUDE,
                DBContract.Appointments.COLUMN_NAME_LATITUDE,
                DBContract.Appointments.COLUMN_NAME_LOC_AREA,
                DBContract.Appointments.COLUMN_NAME_ZOOM_LEVEL,
                DBContract.Appointments.COLUMN_NAME_TIME,
                DBContract.Appointments.COLUMN_NAME_LINEKEY,
        };

        String selection = DBContract.Appointments._ID + " = ?";

        String[] selectionArgs = {
            Long.toString(id)
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DBContract.Appointments.COLUMN_NAME_TIME + " ASC";

        Cursor c = db.query(
                DBContract.Appointments.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                      // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        return c;
    }








    public void insertAppointmentLinekey(String linekey, long id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Appointments.COLUMN_NAME_LINEKEY, linekey);
        String selection = DBContract.Appointments._ID+" = ?";
        String[] selectionArgs = {
            Long.toString(id)
        };
        db.update(DBContract.Appointments.TABLE_NAME, values, selection, selectionArgs);
    }









    // Returns all appointments starting with the soonest
    public Cursor getAllAppointments() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DBContract.Appointments._ID,
                DBContract.Appointments.COLUMN_NAME_TITLE,
                DBContract.Appointments.COLUMN_NAME_CREATED,
                DBContract.Appointments.COLUMN_NAME_LONGITUDE,
                DBContract.Appointments.COLUMN_NAME_LATITUDE,
                DBContract.Appointments.COLUMN_NAME_LOC_AREA,
                DBContract.Appointments.COLUMN_NAME_ZOOM_LEVEL,
                DBContract.Appointments.COLUMN_NAME_TIME
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DBContract.Appointments.COLUMN_NAME_TIME + " ASC";

        Cursor c = db.query(
                DBContract.Appointments.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c;
    }
}
