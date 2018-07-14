package horusai.masterapp.initiation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import horusai.masterapp.R;

public class ConfigureTypeUser extends AppCompatActivity {

    private ViewPager slideView;
    private LinearLayout dotLayout;
    private SliderAdapter sliderAdapt;
    private TextView[] dots;
    private Button finishBtn;
    private Button homeBtn;
    private TextView signInBtn;
    private boolean stillRegistering=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open initiation_configure_type_user

        setContentView(R.layout.initiation_configure_type_user);

        // Create objects

        slideView = findViewById(R.id.initiationConfigureTypeUser_Slideview);
        dotLayout = findViewById(R.id.initiationConfigureTypeUser_DotLayout);
        finishBtn = findViewById(R.id.initiationConfigureTypeUser_NextBtn);
        homeBtn = findViewById(R.id.initiationConfigureTypeUser_BackBtn);
        signInBtn = findViewById(R.id.initiationConfigureTypeUser_SignInBtn);

        sliderAdapt= new SliderAdapter(ConfigureTypeUser.this);

        slideView.setAdapter(sliderAdapt);

        addDotsIndicator(0);

        slideView.addOnPageChangeListener(viewListener);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!stillRegistering){

                    Intent registerIntent = new Intent(ConfigureTypeUser.this,Telephone.class);
                    startActivityForResult(registerIntent,10);

                }
                else{

                    Intent registerIntent = new Intent(ConfigureTypeUser.this,Register.class);
                    startActivity(registerIntent);

                }

            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.not_movable,R.anim.slide_down);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch log in

                Intent loginIntent = new Intent(ConfigureTypeUser.this,Login.class);
                startActivity(loginIntent);
                finish();
                overridePendingTransition(R.anim.slide_up,R.anim.not_movable);
            }
        });

    }

    public void addDotsIndicator(int position){

        dots = new TextView[3];
        dotLayout.removeAllViews();

        for(int i = 0; i< dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(24);
            dots[i].setTextColor(getResources().getColor(R.color.colorGreenSoft));

            dotLayout.addView(dots[i]);

        }

        if(dots.length > 0){

            dots[position].setTextColor(getResources().getColor(R.color.colorMain));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

            hideKeyboard(ConfigureTypeUser.this);

            if(position==0){

                finishBtn.setEnabled(false);
                finishBtn.setVisibility(View.INVISIBLE);

                homeBtn.setEnabled(true);
                homeBtn.setVisibility(View.VISIBLE);

            } else if (position == dots.length -1){

                finishBtn.setEnabled(true);
                finishBtn.setVisibility(View.VISIBLE);

                homeBtn.setEnabled(false);
                homeBtn.setVisibility(View.INVISIBLE);

            }
            else{

                finishBtn.setEnabled(false);
                finishBtn.setVisibility(View.INVISIBLE);

                homeBtn.setEnabled(false);
                homeBtn.setVisibility(View.INVISIBLE);

            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {

            if (resultCode == RESULT_OK) {

                stillRegistering = true;

            }
        }
    }

}
