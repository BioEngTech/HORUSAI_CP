package com.example.daniel.seriousapp.utils;

import android.widget.TextView;

/**
 * Created by joaosousa on 26/05/18.
 */

public class Logger implements ILogger {
    @Override
    public void addMessageToTextView(TextView logTextView, String message) {
        // Clear log after 100 messages
        logTextView.setText(logTextView.getLineCount() > 100 ? message : message + "\n" + logTextView.getText());
    }
}
