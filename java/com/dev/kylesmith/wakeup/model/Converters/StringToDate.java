package com.dev.kylesmith.wakeup.model.Converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kylesmith on 1/12/15.
 */
public class StringToDate {
    public Date StringToDate(String date){
        DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.ENGLISH);
        Date result = new Date();
        try {
            result = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
