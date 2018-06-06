package horusai.masterapp.login_register_package;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

//import com.hbb20.CountryCodePicker;

import horusai.masterapp.R;

public class telephone extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private Button send_btn;
    private Button signin_btn;
    private EditText telephone_number_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.telephone_layout);

        // Create objects

        send_btn = findViewById(R.id.telephone_layout_send_btn);
        signin_btn = findViewById(R.id.telephone_layout_sign_in_btn);
        telephone_number_text = findViewById(R.id.telephone_layout_number);

        // Make buttons and views respond to a click

        signin_btn.setOnClickListener(this);
        send_btn.setOnClickListener(this);

        // Make layout respond to focus

        telephone_number_text.setOnFocusChangeListener(this);

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

            // Launch confirm code activity

            Intent sendIntent = new Intent(telephone.this,confirmationCode.class);
            startActivity(sendIntent);
            finish();

        }

        else if(v.getId()==R.id.telephone_layout_sign_in_btn) {

            // Launch login activity

            Intent loginIntent = new Intent(telephone.this,login.class);
            startActivity(loginIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
