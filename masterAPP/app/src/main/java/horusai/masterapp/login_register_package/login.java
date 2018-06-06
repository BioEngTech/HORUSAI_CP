package horusai.masterapp.login_register_package;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import horusai.masterapp.R;

public class login extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private ImageView cross_pass;
    private EditText password_text;
    private ImageView cross_email;
    private EditText email_text;
    private Button login_btn;
    private Button signup_btn;
    private Button lostpass_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.login_layout);

        // Create objects

        cross_email = findViewById(R.id.login_layout_close_cross_email);
        cross_pass = findViewById(R.id.login_layout_close_cross_pass);
        email_text = findViewById(R.id.login_layout_username);
        password_text = findViewById(R.id.login_layout_password);
        login_btn = findViewById(R.id.login_layout_login_btn);
        signup_btn = findViewById(R.id.login_layout_sign_up_btn);
        lostpass_btn = findViewById(R.id.login_layout_forgot_pass);


        // Make buttons and views respond to a click

        signup_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        lostpass_btn.setOnClickListener(this);
        cross_email.setOnClickListener(this);
        cross_pass.setOnClickListener(this);

        // Make layout respond to focus

        email_text.setOnFocusChangeListener(this);
        password_text.setOnFocusChangeListener(this);

        // Make edit_texts respond to writing

        email_text.addTextChangedListener(generalTextWatcher);
        password_text.addTextChangedListener(generalTextWatcher);

        // Hiding cross button before start typing on EditText.

        cross_email.setVisibility(View.GONE);
        cross_pass.setVisibility(View.GONE);

        // Disable login button until something is written on username/password

        login_btn.setEnabled(false);
        login_btn.setAlpha(0.4f);

        }

        // Implement TextWatcher

        private TextWatcher generalTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Change user_cross visibility

                if (email_text.getText().hashCode() == s.hashCode())
                {
                    if (email_text.getText().length()==0){

                        cross_email.setVisibility(View.GONE);
                    }

                    else{

                        cross_email.setVisibility(View.VISIBLE);

                    }
                }

                // Change pass_cross visibility

                else if (password_text.getText().hashCode() == s.hashCode())
                {
                    if (password_text.getText().length()==0){

                        cross_pass.setVisibility(View.GONE);
                    }

                    else{

                        cross_pass.setVisibility(View.VISIBLE);

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                // Change login btn availability

                if (email_text.getText().length()!=0 && password_text.getText().length()!=0){

                    login_btn.setEnabled(true);
                    login_btn.setAlpha(1f);

                }

                else{

                    login_btn.setEnabled(false);
                    login_btn.setAlpha(0.4f);

                }

            }
        };

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

            if(v.getId()==R.id.login_layout_sign_up_btn) {

                // Launch phone(register) activity

                Intent sign_up_Intent = new Intent(login.this,telephone.class);
                startActivity(sign_up_Intent);
                finish();

            }

            else if(v.getId()==R.id.login_layout_forgot_pass) {

                // Forgot Password

                Intent forgotpasswordIntent = new Intent(login.this,forgotPassword.class);
                startActivity(forgotpasswordIntent);
                finish();
            }

            else if(v.getId()==R.id.login_layout_login_btn) {

                // Try to Log In

                Toast.makeText(login.this,"Welcome back to Vigi! Login successful", Toast.LENGTH_SHORT).show();

            }

            else if(v.getId()==R.id.login_layout_close_cross_email) {

                // Clear email_text and cross.

                email_text.getText().clear();
                cross_email.setVisibility(View.GONE);

            }

            else if(v.getId()==R.id.login_layout_close_cross_pass) {

                // Clear password_text and cross.

                password_text.getText().clear();
                cross_pass.setVisibility(View.GONE);

            }

        }

}

