package com.dev.kylesmith.wakeup.model.Location;

import android.util.Log;

/**
 * Created by kylesmith on 1/12/15.
 */
public class DistanceConverter {
    private static final double E = 21638.8;;

    public static float ZoomLevelToDistance(Float z){
        float d = (float) (E / Math.pow(2, z - 1));
        return d;
    }

    public static float DistanceToZoomLevel(Float d){
        byte z=1;
        double E = 40075;
        Log.i("Astrology", "result: " + (Math.log(E / d) / Math.log(2) + 1));
        z = (byte) Math.round(Math.log(E/d)/Math.log(2)+1);
        // to avoid exeptions
        if (z>21) z=21;
        if (z<1) z =1;
        return z;
    }
}

