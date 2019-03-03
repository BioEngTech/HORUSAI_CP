package vigi.care_provider.view.authentication.home;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.stream.IntStream;

import vigi.care_provider.R;
import vigi.care_provider.view.authentication.home.components.HomeAuthAnimatorListener;
import vigi.care_provider.view.authentication.home.components.HomeAuthSliderAdapter;
import vigi.care_provider.view.authentication.registration.RegisterActivity;
import vigi.care_provider.view.authentication.login.LoginActivity;
import vigi.care_provider.view.vigi.activity.VigiActivity;
import vigi.care_provider.view.vigi.web.VigiHtml;

public class HomeAuthActivity extends AppCompatActivity implements VigiActivity {

    private static String TAG = HomeAuthActivity.class.getName();
    private static int REQUESTCODE = 1;

    private ViewPager slideView;
    private LinearLayout dotLayout;
    private Button loginBtn;
    private Button registerBtn;
    private ProgressBar circleView;
    private ValueAnimator animator;
    private LinearLayout background;
    private LinearLayout buttonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initiation_home);

        setupUiComponents();

        addDotsIndicator(0);

        setupAnimations();
        setupClickListeners();

        background.requestFocus();
    }

    @Override
    public void setupUiComponents() {
        slideView = findViewById(R.id.initiationHome_slideView);
        dotLayout = findViewById(R.id.initiationHome_dotLayout);
        loginBtn = findViewById(R.id.initiationHome_loginButton);
        registerBtn = findViewById(R.id.initiationHome_registerButton);
        circleView = findViewById(R.id.initiationHome_progressCircle);
        background = findViewById(R.id.initiationHome);
        buttonsLayout = findViewById(R.id.initiationHome_buttonsLayout);

        HomeAuthPageChangeListener viewListener = new HomeAuthPageChangeListener();
        HomeAuthSliderAdapter sliderAdapt = new HomeAuthSliderAdapter(HomeAuthActivity.this);

        slideView.setAdapter(sliderAdapt);
        slideView.addOnPageChangeListener(viewListener);
    }

    private void addDotsIndicator(int position){
        dotLayout.removeAllViews();

        IntStream.range(0, 3).forEach(i -> {
            TextView dot = new TextView(HomeAuthActivity.this);
            dot.setText(VigiHtml.fromHtml("&#8226;"));
            dot.setTextSize(24);
            dot.setTextColor(ContextCompat.getColor(HomeAuthActivity.this,
                    i == position ? R.color.colorMain : R.color.colorBlueSoft));
            dotLayout.addView(dot);
        });
    }

    private void setupAnimations() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(800);
        getWindow().setSharedElementEnterTransition(bounds);

        Animation downToUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        downToUp.setDuration(800);

        Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        rightToLeft.setDuration(1000);

        buttonsLayout.setAnimation(downToUp);
        dotLayout.setAnimation(downToUp);
        slideView.setAnimation(rightToLeft);

        animator = ValueAnimator.ofInt(0, circleView.getMax());
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> circleView.setProgress((Integer)animation.getAnimatedValue()));
        animator.addListener(new HomeAuthAnimatorListener());
        animator.start();
    }

    @Override
    public void setupClickListeners() {
        loginBtn.setOnClickListener(ignoredView -> goToActivityOverridingTransition(LoginActivity.class));
        registerBtn.setOnClickListener(ignoredView -> goToActivityOverridingTransition(RegisterActivity.class));
    }

    private void goToActivityOverridingTransition(Class<? extends Activity> activity) {
        Intent registerIntent = new Intent(HomeAuthActivity.this, activity);
        startActivityForResult(registerIntent, REQUESTCODE);
        overridePendingTransition(R.anim.slide_up, R.anim.not_movable);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                animator.start();
            }
        }
    }

    private class HomeAuthPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    }
}
