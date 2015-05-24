package com.dev.kylesmith.wakeup.model.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kylesmith on 1/12/15.
 */
public class CheckIn {
    private LatLng cur_loc;
    private LatLng appt_loc;
    private Date appt_time;
    private float appt_loc_boxSize;

    public CheckIn(LatLng cur_loc, LatLng appt_loc, Date appt_time, float appt_loc_boxSize){
        this.cur_loc = cur_loc;
        this.appt_loc = appt_loc;
        this.appt_time = appt_time;
        this.appt_loc_boxSize = appt_loc_boxSize;
    }


    public String isValid(){
        String error = "";

        // Check proximity
        if(!isNearEnough()) error = "You're not close enough";

        // Check time
        String message = isOnTime();
        if(!message.equals("")) error = message;

        // Pass back any errors
        return error;
    }






    private boolean isNearEnough(){
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        double distance = distanceCalculator.distance(cur_loc.latitude,
                cur_loc.longitude,
                appt_loc.latitude,
                appt_loc.longitude,
                'M');

        if(distance <= appt_loc_boxSize/2) return true;
        else return false;
    }






    // Gives user a +-5 minute window to be on time
    private String isOnTime(){
        int timeLenience = 5;
        Calendar futureTime = Calendar.getInstance();
        futureTime.setTime(appt_time);
        futureTime.add(Calendar.MINUTE, timeLenience);

        Calendar pastTime = Calendar.getInstance();
        pastTime.setTime(appt_time);
        pastTime.add(Calendar.MINUTE, -timeLenience);

        Date curTime = new Date();


        if(curTime.after(futureTime.getTime())) return "You are too late";
        else if(curTime.before(pastTime.getTime())) return "You are too early";
        return "";
    }
}
