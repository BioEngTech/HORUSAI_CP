package vigi.patient.view.authentication.home;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import java.util.ArrayList;
import vigi.patient.view.authentication.home.viewHolder.HomeAuthSliderAdapter;
import vigi.patient.view.authentication.registration.RegisterActivity;
import vigi.patient.view.authentication.login.LoginActivity;
import vigi.patient.view.authentication.home.components.HomeAuthAnimatorListener;
import vigi.patient.view.vigi.activity.VigiActivity;
import vigi.patient.view.vigi.web.VigiHtml;
import vigi.patient.R;

import java.util.stream.IntStream;

public class HomeAuthActivity extends AppCompatActivity implements VigiActivity {

    private static String TAG = HomeAuthActivity.class.getName();
    private static int REQUESTCODE = 1;

    private ViewPager slideView;
    private LinearLayout dotLayout;
    private HomeAuthSliderAdapter adapter;
    private TextView[] dots;
    private Button loginBtn;
    private Button registerBtn;
    private ProgressBar circleView;
    private ValueAnimator animator;
    private LinearLayout background;
    private LinearLayout buttonsLayout;
    private ArrayList<Drawable> sliderImages = new ArrayList<>();
    private ArrayList<String> sliderDescriptions = new ArrayList<>();
    private ArrayList<Integer> sliderImageSizes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout
        setContentView(R.layout.authentication_home);

        setupUiComponents();

        addDotsIndicator(0);

        setupAnimations();
        setupClickListeners();

        background.requestFocus();
    }

    @Override
    public void setupUiComponents() {
        slideView = findViewById(R.id.slide_view);
        dotLayout = findViewById(R.id.dot_layout);
        loginBtn = findViewById(R.id.login_button);
        registerBtn = findViewById(R.id.register_button);
        circleView = findViewById(R.id.progress_circle);
        background = findViewById(R.id.background);
        buttonsLayout = findViewById(R.id.buttons_layout);

        HomeAuthPageChangeListener viewListener = new HomeAuthPageChangeListener();

        // Set up images and descriptions to show on the slider
        sliderImages.add(0,getResources().getDrawable(R.drawable.image_medical_kit));
        sliderDescriptions.add(0,"Get the help you need, right in the corner.");
        sliderImageSizes.add(0,75);
        sliderImages.add(1,getResources().getDrawable(R.drawable.image_staff));
        sliderDescriptions.add(1,"Explore our amazing staff, always ready to assist you");
        sliderImageSizes.add(1,100);
        HomeAuthSliderAdapter adapter = new HomeAuthSliderAdapter(this,sliderImages,sliderDescriptions,sliderImageSizes);

        slideView.setAdapter(adapter);
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
        animator.setDuration(1200);
        animator.addUpdateListener(animation -> circleView.setProgress((Integer)animation.getAnimatedValue()));
        animator.addListener(new HomeAuthAnimatorListener());
        animator.start();

        loginBtn.setOnClickListener(v -> {
            Intent loginIntent = new Intent(HomeAuthActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            overridePendingTransition(R.anim.slide_up,R.anim.not_movable);
        });

        registerBtn.setOnClickListener(v -> {
            Intent registerIntent = new Intent(HomeAuthActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
            overridePendingTransition(R.anim.slide_up,R.anim.not_movable);
        });

        background.requestFocus();
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

    private class HomeAuthPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    @Override
    public void onResume(){
        super.onResume();
        animator.start();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
