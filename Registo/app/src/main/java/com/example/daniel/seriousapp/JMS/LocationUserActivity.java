package com.example.daniel.seriousapp.JMS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.daniel.seriousapp.R;
import com.example.daniel.seriousapp.utils.EventScenario;
import com.example.daniel.seriousapp.utils.ILocationMessage;
import com.example.daniel.seriousapp.utils.LocationWrapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.HashMap;

/**
 * Created by joaosousa on 31/05/18.
 */

public abstract class LocationUserActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ILocationMessage {

    LocationRequest mLocationRequest;

    PendingResult<LocationSettingsResult> result;
    protected ProgressDialog dialog;
    protected GoogleApiClient mGoogleApiClient;
    protected final static int REQUEST_AND_SEND_LOCATION = 100;

    protected IResultCallback myResultCallback;

    protected JMSWrapper jmsWrapper;

    protected static Location sLocation;

    protected void setMyResultCallback(IResultCallback myResultCallback) {
        this.myResultCallback = myResultCallback;
    }

    public static void setLocation(Location location) {
        sLocation = location;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jmsWrapper = new JMSWrapper(this);
        dialog = new ProgressDialog(this);

        //Default behaviour
        setMyResultCallback(new DefaultResultCallback());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(myResultCallback);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (dialog.isShowing()) dialog.dismiss();
        if (requestCode == REQUEST_AND_SEND_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(LocationUserActivity.this, "Location enabled by user! Sending coordinates", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(LocationUserActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void sendJMSObject(Location location) {
        jmsWrapper.sendMessage(getApplicationContext(), new JMSObject(new LocationWrapper(location), EventScenario.CRASH,
        "He is a freaking alcoholic", "ALCOHOLIC dude"));
    }

    protected synchronized void connectGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    protected boolean isGoogleApiClientConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    protected void setDialog(ProgressDialog dialog) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }


    interface IResultCallback<R> extends ResultCallback {
        void afterResult();
    }

    protected class DefaultResultCallback implements IResultCallback {
        @Override
        public void onResult(@NonNull Result result) {
            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    setDialog(dialog);
                    dialog.show();
                    try {
                        // Check the result in onActivityResult().
                        status.startResolutionForResult(LocationUserActivity.this,
                                REQUEST_AND_SEND_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }

            afterResult();
        }

        @Override
        public void afterResult() {
            //Mock method body
        }
    }
}
