package com.example.daniel.seriousapp.utils;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by joaosousa on 03/06/18.
 */

public class LocationWrapper implements Serializable{
    private double latitude;
    private double longitude;
    private double altitude;

    public LocationWrapper(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.altitude = location.getAltitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
