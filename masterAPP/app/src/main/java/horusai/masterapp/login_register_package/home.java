package horusai.masterapp.login_register_package;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import horusai.masterapp.R;


public class home extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout rl1, rl2;
    Animation uptodown,downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open home_layout

        setContentView(R.layout.home_layout);

        // Set up animation

        rl1 = findViewById(R.id.rl1);
        rl2 = findViewById(R.id.rl2);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);

        rl1.setAnimation(uptodown);
        rl2.setAnimation(downtoup);

        // Set existing buttons

        Button sign_up = findViewById(R.id.home_layout_register_btn);
        Button log_in = findViewById(R.id.home_layout_login_btn);

        // calling onClick() method

        sign_up.setOnClickListener(this);
        log_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.home_layout_register_btn) {

            // Launch sign up activity

            Intent signupIntent = new Intent(home.this,telephone.class );
            startActivity(signupIntent);
            finish();
        }

        else if(v.getId()==R.id.home_layout_login_btn) {

            // Launch login activity

            Intent loginIntent = new Intent(home.this,login.class);
            startActivity(loginIntent);
            finish();
        }

    }
}
