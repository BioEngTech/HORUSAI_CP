package com.example.daniel.seriousapp.JMS;

import javax.jms.JMSException;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.seriousapp.R;
import com.example.daniel.seriousapp.utils.EventScenario;
import com.example.daniel.seriousapp.utils.ILocationMessage;
import com.example.daniel.seriousapp.utils.ILogger;
import com.example.daniel.seriousapp.utils.LocationListenerImpl;
import com.example.daniel.seriousapp.utils.LocationWrapper;
import com.example.daniel.seriousapp.utils.OnSwipeTouchListener;

import java.util.HashMap;

public class ActivityTest extends LocationUserActivity {

    private static String TAG = "com.kaazing.gateway.jms.client.android.demo";

    private Button sendBtn;
    private TextView logTextView;

    private ImageView leftArrow;
    private ImageView upArrow;
    private ImageView rightArrow;
    private ImageView downArrow;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private ILocationMessage locationMessage;

    private NetworkInfo currentNetworkInfo;

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()) {
                try {
                    jmsWrapper.createConnectionFactory();
                    logMessage(getResources().getString(R.string.connecting));
//                  logMessage(getResources().getString(R.string.subscribe) + " - " + getResources().getString(R.string.TOPIC_NAME));

                    /* CONNECTION MADE HERE */
                    jmsWrapper.doJMSMagic(getApplicationContext());

                } catch (JMSException e) {
                    e.printStackTrace();
                    Toast.makeText(ActivityTest.this, "EXCEPTION: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("No connection")
                        .setMessage("Please connect to the Internet")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    };

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_test);

        leftArrow = findViewById(R.id.leftArrow);
        upArrow = findViewById(R.id.upArrow);
        rightArrow = findViewById(R.id.rightArrow);
        downArrow = findViewById(R.id.downArrow);

//        setMyResultCallback(new MyResultCallback());
        sendBtn = findViewById(R.id.send_btn);
        logTextView = findViewById(R.id.logView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = LocationListenerImpl.getInstanceForContext(this);

        jmsWrapper.setLogTextView(logTextView);

        locationMessage = new LocationMessage();

        this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        sendBtn.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        sendBtn.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            public void onSwipeTop() {
                upArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
                downArrow.setVisibility(View.INVISIBLE);
                leftArrow.setVisibility(View.INVISIBLE);
                Toast.makeText(ActivityTest.this, "top", Toast.LENGTH_SHORT).show();
            }
            @SuppressLint("MissingPermission")
            @Override
            public void onSwipeRight() {
                rightArrow.setVisibility(View.VISIBLE);
                downArrow.setVisibility(View.INVISIBLE);
                leftArrow.setVisibility(View.INVISIBLE);
                upArrow.setVisibility(View.INVISIBLE);

                if (!currentNetworkInfo.isConnected()) {
                    Toast.makeText(ActivityTest.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!isGoogleApiClientConnected()) {
                    connectGoogleApiClient();
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                } else if (sLocation != null){
                    //Send value in cache
                    locationMessage.sendJMSObject(sLocation);
                }
                Toast.makeText(ActivityTest.this, "right", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeLeft() {
                leftArrow.setVisibility(View.VISIBLE);
                upArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
                downArrow.setVisibility(View.INVISIBLE);
                Toast.makeText(ActivityTest.this, "left", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeBottom() {
                downArrow.setVisibility(View.VISIBLE);
                leftArrow.setVisibility(View.INVISIBLE);
                upArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
                Toast.makeText(ActivityTest.this, "bottom", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSingleTap() {
                findViewById(R.id.upDrag).setVisibility(View.VISIBLE);
                findViewById(R.id.downDrag).setVisibility(View.VISIBLE);
                findViewById(R.id.leftDrag).setVisibility(View.VISIBLE);
                findViewById(R.id.rightDrag).setVisibility(View.VISIBLE);
            }

            @Override
            public void onTapUp() {
                findViewById(R.id.upDrag).setVisibility(View.INVISIBLE);
                findViewById(R.id.downDrag).setVisibility(View.INVISIBLE);
                findViewById(R.id.leftDrag).setVisibility(View.INVISIBLE);
                findViewById(R.id.rightDrag).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.leftDrag).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP:
                        Toast.makeText(ActivityTest.this, "Left wohoo", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        findViewById(R.id.rightDrag).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP:
                        Toast.makeText(ActivityTest.this, "Right !", Toast.LENGTH_SHORT).show();
                }
                return true;
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
        public void sendJMSObject(Location location) {
            jmsWrapper.sendMessage(getApplicationContext(), new JMSObject(new LocationWrapper(location), EventScenario.CRASH,
                    "He is a freaking alcoholic", "ALCOHOLIC dude"));
        }
    }
}


