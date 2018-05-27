package com.example.daniel.seriousapp.phone_call;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.daniel.seriousapp.R;

public class phone_call extends Activity {
    private TelephonyManager telephonyManager;
    final Context context = this;
    private Button button;
    private static final int REQUEST_PHONE_CALL = 1;
    private static final String TAG = "verificar";


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_call);

        button = (Button) findViewById(R.id.buttonCall);
        Log.d(TAG, "passa1");

        PhoneCallListener phoneListener = new PhoneCallListener();
        Log.d(TAG, "passa2");

        telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(TAG, "passa3");

        if (isTelephonyEnabled()) {
            Log.d(TAG, "Telephony is enabled");
            // ToDo: Check for phone permission.
            // ToDo: Register the PhoneStateListener.
        } else {
            Toast.makeText(this,
                    "TELEPHONY NOT ENABLED! ",
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "TELEPHONY NOT ENABLED! ");
            // Disable the call button
        }


        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);


        // add button listener
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+351915235232"));
                Log.d(TAG, "passa4");

                if (ActivityCompat.checkSelfPermission(phone_call.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(phone_call.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                } else {
                    Log.d(TAG, "iniciou chamada");

                    startActivity(callIntent);
                }
            }

        });

    }


    private boolean isTelephonyEnabled() {
        if (telephonyManager != null) {
            if (telephonyManager.getSimState() ==
                    telephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }


    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

}

