package com.example.daniel.seriousapp.JMS;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.seriousapp.R;
import com.example.daniel.seriousapp.utils.ILogger;
import com.example.daniel.seriousapp.utils.JMSWrapper;
import com.kaazing.gateway.jms.client.JmsConnectionFactory;

public class ActivityTest extends FragmentActivity {

    private static String TAG = "com.kaazing.gateway.jms.client.android.demo";

    private Button sendBtn;
    private TextView logTextView;

    private JMSWrapper jmsWrapper;

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_test);

        sendBtn = findViewById(R.id.send_btn);
        logTextView = findViewById(R.id.logView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        jmsWrapper = new JMSWrapper(this);
        jmsWrapper.setLogTextView(logTextView);

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
            @Override
            public void onClick(View view) {
                logMessage("Sending something");
                jmsWrapper.sendMessage(getApplicationContext());
            }
        });
    }

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

    public void onDestroy() {
        logMessage(jmsWrapper.disconnect(getApplicationContext()));
        super.onDestroy();
    }

    public void logMessage(final String message) {
        ILogger logger = new com.example.daniel.seriousapp.utils.Logger();
        logger.addMessageToTextView(logTextView, message);
    }
}


