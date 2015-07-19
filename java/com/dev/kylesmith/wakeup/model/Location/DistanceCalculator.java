package com.dev.kylesmith.wakeup.model.Location;

import com.dev.kylesmith.wakeup.util.Convert;

/**
 * Created by kylesmith on 1/12/15.
 */
public class DistanceCalculator {
    public double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(
                Convert.deg2rad(lat1)) * Math.sin(Convert.deg2rad(lat2)) + Math.cos(Convert.deg2rad(lat1)) * Math.cos(Convert.deg2rad(lat2)) * Math.cos(Convert.deg2rad(theta)
        );
        dist = Math.acos(dist);
        dist = Convert.rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }
}
