package horusai.masterapp.login_register_package;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.Toast;
=======
import android.widget.EditText;
>>>>>>> ReiGOIS

//import com.hbb20.CountryCodePicker;

import horusai.masterapp.R;

public class telephone extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

<<<<<<< HEAD
    Button send_btn;
    Button signin_btn;
=======
    private Button send_btn;
    private Button signin_btn;
    private EditText telephone_number_text;
>>>>>>> ReiGOIS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.telephone_layout);

        // Create objects

        send_btn = findViewById(R.id.telephone_layout_send_btn);
        signin_btn = findViewById(R.id.telephone_layout_sign_in_btn);
<<<<<<< HEAD
=======
        telephone_number_text = findViewById(R.id.telephone_layout_number);
>>>>>>> ReiGOIS

        // Make buttons and views respond to a click

        signin_btn.setOnClickListener(this);
        send_btn.setOnClickListener(this);

<<<<<<< HEAD
        //CountryCodePicker ccp;

        //ccp = (CountryCodePicker) findViewById(R.id.ccp);
=======
        // Make layout respond to focus

        telephone_number_text.setOnFocusChangeListener(this);
>>>>>>> ReiGOIS

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

<<<<<<< HEAD
            // Launch register activity

            Intent sendIntent = new Intent(telephone.this,confirmation_code.class);
            startActivity(sendIntent);
=======
            // Launch confirm code activity

            Intent sendIntent = new Intent(telephone.this,confirmationCode.class);
            startActivity(sendIntent);
            finish();
>>>>>>> ReiGOIS

        }

        else if(v.getId()==R.id.telephone_layout_sign_in_btn) {

<<<<<<< HEAD
            // Launch register activity

            Intent loginIntent = new Intent(telephone.this,login.class);
            startActivity(loginIntent);
            super.finish();
=======
            // Launch login activity

            Intent loginIntent = new Intent(telephone.this,login.class);
            startActivity(loginIntent);
            finish();
>>>>>>> ReiGOIS
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
