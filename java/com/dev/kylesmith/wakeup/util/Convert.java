package com.dev.kylesmith.wakeup.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kylesmith on 7/18/15.
 */
public class Convert {
    public static double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double radToDeg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Date stringToDate(String date){
        DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.ENGLISH);
        Date result = new Date();
        try {
            result = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap uriToBitmap(Uri u, Context context){
        Bitmap bitmap, scaled = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), u);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return scaled;
    }
}
