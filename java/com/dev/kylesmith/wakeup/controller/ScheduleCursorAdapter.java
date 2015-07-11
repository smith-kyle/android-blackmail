package com.dev.kylesmith.wakeup.controller;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Converters.StringToDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kylesmith on 1/23/15.
 */
public class ScheduleCursorAdapter extends SimpleCursorAdapter{
    StringToDate STD = new StringToDate();
    ScheduleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to){
        super(context, layout, c, from, to);
    }

    @Override
    public void setViewText(TextView v, String text) {
        if(v.getId() == R.id.appt_time){
            Date apptDatetime = StringToDate.Convert(text);
            DateFormat dateFormat = new SimpleDateFormat("MMM d hh:mm a");

            // Check if the appointment is too late
            Calendar today = Calendar.getInstance();
            today.add(Calendar.MINUTE, 5);
            if(today.before(apptDatetime)){
                text = "Appointment has already passed";
                v.setTextColor(Color.RED);
            }
            else {
                text = dateFormat.format(apptDatetime);
            }
        }
        v.setText(text);
    }
}
