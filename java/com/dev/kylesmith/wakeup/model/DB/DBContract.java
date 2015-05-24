package com.dev.kylesmith.wakeup.model.DB;

import android.provider.BaseColumns;

/**
 * Created by kylesmith on 1/8/15.
 */
public final class DBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DBContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Appointments implements BaseColumns {
        public static final String TABLE_NAME = "appointments";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CREATED = "created";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LOC_AREA = "loc_area";
        public static final String COLUMN_NAME_LINEKEY = "linekey";
        public static final String COLUMN_NAME_ZOOM_LEVEL = "zoom_level";
        public static final String COLUMN_NAME_NULLABLE = "created";
    }


    public static abstract class User implements BaseColumns{
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_PHONE_NUM = "phone_num";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_PHOTO_LOCATION = "photo_uri";
        public static final String COLUMN_NAME_NULLABLE = "created";
    }
}
