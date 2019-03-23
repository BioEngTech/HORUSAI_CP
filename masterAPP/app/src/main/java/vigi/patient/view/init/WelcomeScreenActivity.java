package vigi.patient.view.init;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import vigi.patient.R;

import vigi.patient.view.authentication.home.HomeAuthActivity;

@SuppressWarnings("FieldCanBeLocal")
public class WelcomeScreenActivity extends AppCompatActivity {

    private static String TAG = Activity.class.getName();

    private LinearLayout welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout
        setContentView(R.layout.initiation_welcome_screen);

        // Views
        welcomeText = findViewById(R.id.welcome_to_vigi);

        // Set initiation_home after 3 seconds
        Handler handler = new Handler();
        handler.postDelayed(this::launchHomeAuthActivity, 3000);
    }

    private void launchHomeAuthActivity() {
        Intent homeIntent = new Intent(WelcomeScreenActivity.this, HomeAuthActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeScreenActivity.this, welcomeText,"welcomeToVigi");
        startActivity(homeIntent, options.toBundle());
    }

}
