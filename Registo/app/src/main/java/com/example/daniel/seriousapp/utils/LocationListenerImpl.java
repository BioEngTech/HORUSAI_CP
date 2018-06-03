package com.example.daniel.seriousapp.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.example.daniel.seriousapp.JMS.LocationUserActivity;

/**
 * Created by joaosousa on 29/05/18.
 */

public class LocationListenerImpl implements LocationListener {
    /* Singleton Pattern -> only one instance of LocationListener! */
    private static LocationListenerImpl instance;
    private Context mContext;
    private ILocationMessage locationMessage;

    /* Private constructor to prevent instantiation*/
    private LocationListenerImpl(Context context) {
        mContext = context;
    }

    /* Same instance is used but with different contexts */
    public static LocationListener getInstanceForContext(Context context) {
        if (instance == null) {
            synchronized (LocationListener.class) {
                instance = new LocationListenerImpl(context);
            }
        }
        return instance;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Only activities that extend LocationUserActivity!
        ((LocationUserActivity) mContext).setLocation(location);
        ((LocationUserActivity) mContext).sendJMSObject(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
