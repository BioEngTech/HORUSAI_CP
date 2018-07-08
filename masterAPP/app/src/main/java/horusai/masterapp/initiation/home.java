package horusai.masterapp.initiation;

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

    RelativeLayout buttonsMove, logoMove;
    Animation upToDown, downToUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open initiation_home

        setContentView(R.layout.initiation_home);
        
        // Set up animation

        logoMove = findViewById(R.id.initiationHome_MainLayout);
        buttonsMove = findViewById(R.id.initiationHome_FooterLayout);

        upToDown = AnimationUtils.loadAnimation(this,R.anim.up_to_down);
        downToUp = AnimationUtils.loadAnimation(this,R.anim.down_to_up);

        logoMove.setAnimation(upToDown);
        buttonsMove.setAnimation(downToUp);

        // Set existing buttons

        Button sign_up = findViewById(R.id.initiationHome_RegisterBtn);
        Button log_in = findViewById(R.id.initiationHome_LoginBtn);

        // calling onClick() method

        sign_up.setOnClickListener(this);
        log_in.setOnClickListener(this);

        }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.initiationHome_RegisterBtn) {

            // Launch sign up activity

            Intent configureTypeUserIntent = new Intent(home.this,configureTypeUser.class );
            startActivity(configureTypeUserIntent);
            overridePendingTransition(R.anim.slide_up,R.anim.not_movable);

        }

        else if(v.getId()==R.id.initiationHome_LoginBtn) {

            // Launch login activity

            Intent loginIntent = new Intent(home.this,login.class);
            startActivity(loginIntent);
            overridePendingTransition(R.anim.slide_up,R.anim.not_movable);
        }

    }
}
