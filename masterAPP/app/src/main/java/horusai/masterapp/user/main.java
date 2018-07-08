package horusai.masterapp.user;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import horusai.masterapp.R;
import horusai.masterapp.utils.bottomNavigationViewHelper;

public class main extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout mainFrame;
    private horusai.masterapp.user.ratingFragment ratingFragment;
    private horusai.masterapp.user.homeFragment homeFragment;
    private horusai.masterapp.user.eventsFragment eventsFragment;
    private horusai.masterapp.user.accountFragment accountFragment;

    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        mainFrame = findViewById(R.id.userMain_MainFrame);
        bottomNavigationView = findViewById(R.id.userMain_BottomNav);
        bottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        homeFragment = new homeFragment();
        ratingFragment = new ratingFragment();
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

                    case R.id.navigation_rating:

                        setFragment(ratingFragment);

                        return true;

                    case R.id.navigation_events:

                        setFragment(eventsFragment);

                        return true;

                    case R.id.navigation_account:

                        setFragment(accountFragment);

                        return true;

                    default:

                        return false;
                }
            }
        });


    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.userMain_MainFrame,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
