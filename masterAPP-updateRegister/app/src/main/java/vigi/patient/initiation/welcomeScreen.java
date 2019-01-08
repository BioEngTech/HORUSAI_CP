package vigi.patient.initiation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import vigi.patient.R;
import vigi.patient.user.main;

@SuppressWarnings("FieldCanBeLocal")
public class welcomeScreen extends AppCompatActivity {

    private static String TAG = "welcomeClass";

    private LinearLayout welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout

        setContentView(R.layout.initiation_welcome_screen);

        // Objects

        welcomeText = findViewById(R.id.initiationWelcomeScreen_welcomeToVigi);

        // Set initiation_home after 3 seconds

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                launchHomeActivity();
            }
        }, 3000);
    }

    private void launchHomeActivity() {

        Intent homeIntent = new Intent(welcomeScreen.this, home.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(welcomeScreen.this, welcomeText,"welcomeToVigi");
        startActivity(homeIntent, options.toBundle());
    }

}
