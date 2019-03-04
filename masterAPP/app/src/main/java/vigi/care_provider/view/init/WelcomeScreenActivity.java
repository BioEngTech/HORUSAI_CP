package vigi.care_provider.view.init;

import android.app.ActivityOptions;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import vigi.care_provider.R;
import vigi.care_provider.view.authentication.home.HomeAuthActivity;

import static vigi.care_provider.view.utils.activity.ActivityUtils.jumpToActivity;


@SuppressWarnings("FieldCanBeLocal")
public class WelcomeScreenActivity extends AppCompatActivity {

    private static String TAG = WelcomeScreenActivity.class.getName();

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
        handler.postDelayed(this::launchHomeAuthActivity, 3000);
    }

    private void launchHomeAuthActivity() {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeScreenActivity.this,
                welcomeText,"welcomeToVigi");
        jumpToActivity(this, HomeAuthActivity.class, false, options.toBundle());
    }

}
