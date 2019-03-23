package vigi.patient.view.authentication.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import vigi.patient.R;

@SuppressWarnings("FieldCanBeLocal")
public class HomeAuthActivity extends AppCompatActivity {

    private static String TAG = Activity.class.getName();

    private ViewPager slideView;
    private LinearLayout dotLayout;
    private HomeAuthSliderAdapter adapter;
    private TextView[] dots;
    private Button loginBtn;
    private Button registerBtn;
    private Animation downToUp;
    private Animation rightToLeft;
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

        // Views
        slideView = findViewById(R.id.slide_view);
        dotLayout = findViewById(R.id.dot_layout);
        loginBtn = findViewById(R.id.login_button);
        registerBtn = findViewById(R.id.register_button);
        circleView = findViewById(R.id.progress_circle);
        background = findViewById(R.id.background);
        buttonsLayout = findViewById(R.id.buttons_layout);

        // Set up images and descriptions to show on the slider
        sliderImages.add(0,getResources().getDrawable(R.drawable.image_medical_kit));
        sliderDescriptions.add(0,"Get the help you need, right in the corner.");
        sliderImageSizes.add(0,75);
        sliderImages.add(1,getResources().getDrawable(R.drawable.image_staff));
        sliderDescriptions.add(1,"Explore our amazing staff, always ready to assist you");
        sliderImageSizes.add(1,100);

        // Set up slide show
        adapter = new HomeAuthSliderAdapter(this,sliderImages,sliderDescriptions,sliderImageSizes);
        slideView.setAdapter(adapter);
        slideView.addOnPageChangeListener(viewListener);
        addDotsIndicator(0);

        // Set up animations
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(800);
        getWindow().setSharedElementEnterTransition(bounds);

        downToUp = AnimationUtils.loadAnimation(this,R.anim.slide_up);
        downToUp.setDuration(800);

        rightToLeft = AnimationUtils.loadAnimation(this,R.anim.slide_in_left);
        rightToLeft.setDuration(1000);

        buttonsLayout.setAnimation(downToUp);
        dotLayout.setAnimation(downToUp);
        slideView.setAnimation(rightToLeft);

        animator = ValueAnimator.ofInt(0, circleView.getMax());
        animator.setDuration(1200);
        animator.addUpdateListener(animation -> circleView.setProgress((Integer)animation.getAnimatedValue()));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
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

    public void addDotsIndicator(int position){

        dots = new TextView[sliderImages.size()];
        dotLayout.removeAllViews();

        for(int i = 0; i< dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(24);
            dots[i].setTextColor(getResources().getColor(R.color.colorBlueSoft));

            dotLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorMain));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
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
