package com.example.daniel.seriousapp.JMS;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.seriousapp.LocationUserActivity;
import com.example.daniel.seriousapp.R;
import com.example.daniel.seriousapp.utils.ILocationMessage;
import com.example.daniel.seriousapp.utils.ILogger;
import com.example.daniel.seriousapp.utils.LocationListenerImpl;

public class ActivityTest extends LocationUserActivity {

    private static String TAG = "com.kaazing.gateway.jms.client.android.demo";

    private Button sendBtn;
    private TextView logTextView;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private ILocationMessage locationMessage;

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_test);

//        setMyResultCallback(new MyResultCallback());
        sendBtn = findViewById(R.id.send_btn);
        logTextView = findViewById(R.id.logView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = LocationListenerImpl.getInstanceForContext(this);

        jmsWrapper.setLogTextView(logTextView);

        locationMessage = new LocationMessage();

        Logger logger = Logger.getLogger("com.careprovider.prototype.vigi");
        logger.setLevel(Level.FINE);

        try {
            jmsWrapper.createConnectionFactory();
            logMessage(getResources().getString(R.string.connecting));
            logMessage(getResources().getString(R.string.subscribe) + " - " + getResources().getString(R.string.TOPIC_NAME));

            /* CONNECTION MADE HERE */
            jmsWrapper.doJMSMagic(getApplicationContext());

        } catch (JMSException e) {
            e.printStackTrace();
            Toast.makeText(ActivityTest.this, "EXCEPTION: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!isGoogleApiClientConnected()) {
                    connectGoogleApiClient();
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                } else if (sLocation != null){
                    //Send value in cache
                    locationMessage.sendLocation(sLocation);
                }
            }
        });
    }

    @Override
    public void onPause() {
        /* Do nothing for now */

//    	if (connection != null) {
//    		dispatchQueue.dispatchAsync(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						connection.stop();
//					} catch (JMSException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//    	}
        super.onPause();
    }

    @Override
    public void onResume() {
        /* Do nothing for now */

//    	if (connection != null) {
//    		dispatchQueue.dispatchAsync(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						connection.start();
//					} catch (JMSException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//    	}
        super.onResume();
    }

    @Override
    public void onDestroy() {
        logMessage(jmsWrapper.disconnect(getApplicationContext()));
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AND_SEND_LOCATION && resultCode == Activity.RESULT_OK) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        }
    }

    public void logMessage(final String message) {
        ILogger logger = new com.example.daniel.seriousapp.utils.Logger();
        logger.addMessageToTextView(logTextView, message);
    }

    class LocationMessage implements ILocationMessage {

        @Override
        public void sendLocation(Location location) {
            StringBuffer stringBuffer = new StringBuffer();
            jmsWrapper.sendMessage(getApplicationContext(),
                    stringBuffer.append("Altitude").append(location.getAltitude()).append("\n")
                            .append("Latitude").append(location.getLatitude()).append("\n")
                            .append("Longitude").append(location.getLongitude()).append("\n")
                            .toString());
        }
    }
}


