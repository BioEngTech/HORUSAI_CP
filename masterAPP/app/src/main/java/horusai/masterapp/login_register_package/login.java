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

<<<<<<< HEAD
    ImageView cross_pass;
    EditText password_text;
    ImageView cross_user;
    EditText username_text;
    Button login_btn;
    Button signup_btn;
    Button lostpass_btn;
=======
    private ImageView cross_pass;
    private EditText password_text;
    private ImageView cross_email;
    private EditText email_text;
    private Button login_btn;
    private Button signup_btn;
    private Button lostpass_btn;
>>>>>>> ReiGOIS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.login_layout);

        // Create objects

<<<<<<< HEAD
        cross_user = findViewById(R.id.login_layout_close_cross_username);
        cross_pass = findViewById(R.id.login_layout_close_cross_pass);
        username_text = findViewById(R.id.login_layout_username);
=======
        cross_email = findViewById(R.id.login_layout_close_cross_email);
        cross_pass = findViewById(R.id.login_layout_close_cross_pass);
        email_text = findViewById(R.id.login_layout_username);
>>>>>>> ReiGOIS
        password_text = findViewById(R.id.login_layout_password);
        login_btn = findViewById(R.id.login_layout_login_btn);
        signup_btn = findViewById(R.id.login_layout_sign_up_btn);
        lostpass_btn = findViewById(R.id.login_layout_forgot_pass);


        // Make buttons and views respond to a click

        signup_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        lostpass_btn.setOnClickListener(this);
<<<<<<< HEAD
        cross_user.setOnClickListener(this);
=======
        cross_email.setOnClickListener(this);
>>>>>>> ReiGOIS
        cross_pass.setOnClickListener(this);

        // Make layout respond to focus

<<<<<<< HEAD
        username_text.setOnFocusChangeListener(this);
=======
        email_text.setOnFocusChangeListener(this);
>>>>>>> ReiGOIS
        password_text.setOnFocusChangeListener(this);

        // Make edit_texts respond to writing

<<<<<<< HEAD
        username_text.addTextChangedListener(generalTextWatcher);
=======
        email_text.addTextChangedListener(generalTextWatcher);
>>>>>>> ReiGOIS
        password_text.addTextChangedListener(generalTextWatcher);

        // Hiding cross button before start typing on EditText.

<<<<<<< HEAD
        cross_user.setVisibility(View.GONE);
=======
        cross_email.setVisibility(View.GONE);
>>>>>>> ReiGOIS
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

<<<<<<< HEAD
                if (username_text.getText().hashCode() == s.hashCode())
                {
                    if (username_text.getText().length()==0){

                        cross_user.setVisibility(View.GONE);
=======
                if (email_text.getText().hashCode() == s.hashCode())
                {
                    if (email_text.getText().length()==0){

                        cross_email.setVisibility(View.GONE);
>>>>>>> ReiGOIS
                    }

                    else{

<<<<<<< HEAD
                        cross_user.setVisibility(View.VISIBLE);
=======
                        cross_email.setVisibility(View.VISIBLE);
>>>>>>> ReiGOIS

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

<<<<<<< HEAD
                if (username_text.getText().length()!=0 && password_text.getText().length()!=0){
=======
                if (email_text.getText().length()!=0 && password_text.getText().length()!=0){
>>>>>>> ReiGOIS

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

<<<<<<< HEAD
                // Launch register activity

                Intent registerIntent = new Intent(login.this,telephone.class);
                startActivity(registerIntent);
=======
                // Launch phone(register) activity

                Intent sign_up_Intent = new Intent(login.this,telephone.class);
                startActivity(sign_up_Intent);
                finish();

>>>>>>> ReiGOIS
            }

            else if(v.getId()==R.id.login_layout_forgot_pass) {

                // Forgot Password

<<<<<<< HEAD
                Toast.makeText(login.this,"Forgot Password missing still!", Toast.LENGTH_SHORT).show();

=======
                Intent forgotpasswordIntent = new Intent(login.this,forgotPassword.class);
                startActivity(forgotpasswordIntent);
                finish();
>>>>>>> ReiGOIS
            }

            else if(v.getId()==R.id.login_layout_login_btn) {

                // Try to Log In

                Toast.makeText(login.this,"Welcome back to Vigi! Login successful", Toast.LENGTH_SHORT).show();

            }

<<<<<<< HEAD
            else if(v.getId()==R.id.login_layout_close_cross_username) {

                // Clear username_text and cross.

                username_text.getText().clear();
                cross_user.setVisibility(View.GONE);
=======
            else if(v.getId()==R.id.login_layout_close_cross_email) {

                // Clear email_text and cross.

                email_text.getText().clear();
                cross_email.setVisibility(View.GONE);
>>>>>>> ReiGOIS

            }

            else if(v.getId()==R.id.login_layout_close_cross_pass) {

                // Clear password_text and cross.

                password_text.getText().clear();
                cross_pass.setVisibility(View.GONE);

            }

        }

}

