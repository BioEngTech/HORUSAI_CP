package vigi.patient.user;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;


import vigi.patient.R;
import vigi.patient.utils.bottomNavigationViewHelper;

public class main extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout mainFrame;
    private healthFragment ratingFragment;
    private vigi.patient.user.homeFragment homeFragment;
    private vigi.patient.user.eventsFragment eventsFragment;
    private vigi.patient.user.accountFragment accountFragment;

    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        mainFrame = findViewById(R.id.userMain_MainFrame);

        bottomNavigationView = findViewById(R.id.userMain_BottomNav);

        // disable shiftMode bottom navigation

        //TODO commented by Daniel
        //bottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        homeFragment = new homeFragment();
        ratingFragment = new healthFragment();
        eventsFragment = new eventsFragment();
        accountFragment = new accountFragment();

        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.navigation_home:

                        setFragment(homeFragment);

                        return true;

                    case R.id.navigation_health:

                        setFragment(ratingFragment);

                        return true;

                    case R.id.navigation_events:

                        setFragment(eventsFragment);

                        return true;

                    case R.id.navigation_timeline:

                        setFragment(accountFragment);

                        return true;

                    default:

                        return false;
                }
            }
        });


        //TODO ask for GPS permission and save that to firebase


    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mainFrame.getId(),fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
