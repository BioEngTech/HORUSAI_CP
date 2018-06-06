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

public class confirmationCode extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private Button continue_btn;
    private Button goback_btn;
    private EditText confirmation_code_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.confirmation_code_layout);

        // Create objects

        continue_btn = findViewById(R.id.confirmation_layout_continue_btn);
        goback_btn = findViewById(R.id.confirmation_layout_go_back_btn);
        confirmation_code_text = findViewById(R.id.confirmation_layout_code);


        // Make buttons and views respond to a click

        goback_btn.setOnClickListener(this);
        continue_btn.setOnClickListener(this);

        // Make layout respond to focus

        confirmation_code_text.setOnFocusChangeListener(this);

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

        if(v.getId()==R.id.confirmation_layout_continue_btn) {

            // Launch registering activity

            Intent registerIntent = new Intent(confirmationCode.this,register.class);
            startActivity(registerIntent);
            finish();
        }

        else if(v.getId()==R.id.confirmation_layout_go_back_btn) {

            // Launch phone activity

            Intent phoneIntent = new Intent(confirmationCode.this,telephone.class);
            startActivity(phoneIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
