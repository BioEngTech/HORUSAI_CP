package horusai.masterapp.login_register_package;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

//import com.hbb20.CountryCodePicker;

import horusai.masterapp.R;

public class telephone extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    Button send_btn;
    Button signin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.telephone_layout);

        // Create objects

        send_btn = findViewById(R.id.telephone_layout_send_btn);
        signin_btn = findViewById(R.id.telephone_layout_sign_in_btn);

        // Make buttons and views respond to a click

        signin_btn.setOnClickListener(this);
        send_btn.setOnClickListener(this);

        //CountryCodePicker ccp;

        //ccp = (CountryCodePicker) findViewById(R.id.ccp);

    }

    // Implement unfocus

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager input =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // Implement funcionalities after clicking buttons

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.telephone_layout_send_btn) {

            // Launch register activity

            Intent sendIntent = new Intent(telephone.this,confirmation_code.class);
            startActivity(sendIntent);

        }

        else if(v.getId()==R.id.telephone_layout_sign_in_btn) {

            // Launch register activity

            Intent loginIntent = new Intent(telephone.this,login.class);
            startActivity(loginIntent);
            super.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
