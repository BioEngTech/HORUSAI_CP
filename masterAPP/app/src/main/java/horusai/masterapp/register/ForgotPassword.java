package horusai.masterapp.register;

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

import horusai.masterapp.R;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private Button send_btn;
    private Button goback_btn;
    private EditText email_text;
    private ImageView cross_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.forgot_password_layout);

        // Create objects

        send_btn = findViewById(R.id.forgot_password_layout_send_btn);
        goback_btn = findViewById(R.id.forgot_password_layout_go_back_btn);
        email_text = findViewById(R.id.forgot_password_layout_email);
        cross_email = findViewById(R.id.forgot_password_layout_close_cross_email);

        // Make buttons and views respond to a click

        goback_btn.setOnClickListener(this);
        send_btn.setOnClickListener(this);
        cross_email.setOnClickListener(this);

        // Make layout respond to focus

        email_text.setOnFocusChangeListener(this);

    // Make edit_texts respond to writing

        email_text.addTextChangedListener(generalTextWatcher);

    // Hiding cross button before start typing on EditText.

        cross_email.setVisibility(View.GONE);

    // Disable Login button until something is written on username/password

        send_btn.setEnabled(false);
        send_btn.setAlpha(0.4f);

    }

    // Implement TextWatcher

    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // Change email_cross visibility

            if (email_text.getText().hashCode() == s.hashCode())
            {
                if (email_text.getText().length()==0){

                    cross_email.setVisibility(View.GONE);
                }

                else{

                    cross_email.setVisibility(View.VISIBLE);

                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            // Change send btn availability

            if (email_text.getText().length()!=0){

                send_btn.setEnabled(true);
                send_btn.setAlpha(1f);

            }

            else{

                send_btn.setEnabled(false);
                send_btn.setAlpha(0.4f);

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

        if(v.getId()==R.id.forgot_password_layout_send_btn) {

            // Send e-mail to user, and if he clicks the link on the e-mail the app opens the user's menu

        }

        else if(v.getId()==R.id.forgot_password_layout_go_back_btn) {

            // Launch Login activity

            Intent loginIntent = new Intent(ForgotPassword.this,Login.class);
            startActivity(loginIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if(v.getId()==R.id.forgot_password_layout_close_cross_email) {

            // Clear username_text and cross.

            email_text.getText().clear();
            cross_email.setVisibility(View.GONE);

        }

    }
}