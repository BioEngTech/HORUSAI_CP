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
import android.widget.Toast;

import horusai.masterapp.R;
import horusai.masterapp.email.MailSender;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private Button sendBtn;
    private Button goBackBtn;
    private EditText emailText;
    private ImageView crossEmail;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.forgot_password_layout);

        // Create objects

        sendBtn = findViewById(R.id.forgot_password_layout_send_btn);
        goBackBtn = findViewById(R.id.forgot_password_layout_go_back_btn);
        emailText = findViewById(R.id.forgot_password_layout_email);
        crossEmail = findViewById(R.id.forgot_password_layout_close_cross_email);

        // Make buttons and views respond to a click

        goBackBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        crossEmail.setOnClickListener(this);

        // Make layout respond to focus

        emailText.setOnFocusChangeListener(this);

    // Make edit_texts respond to writing

        emailText.addTextChangedListener(generalTextWatcher);

    // Hiding cross button before start typing on EditText.

        crossEmail.setVisibility(View.GONE);

    // Disable Login button until something is written on username/password

        sendBtn.setEnabled(false);
        sendBtn.setAlpha(0.4f);

    }

    // Implement TextWatcher

    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // Change email_cross visibility

            if (emailText.getText().hashCode() == s.hashCode())
            {
                if (emailText.getText().length()==0){

                    crossEmail.setVisibility(View.GONE);
                }

                else{

                    crossEmail.setVisibility(View.VISIBLE);

                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            // Change send btn availability

            if (emailText.getText().length()!=0){

                sendBtn.setEnabled(true);
                sendBtn.setAlpha(1f);

            }

            else{

                sendBtn.setEnabled(false);
                sendBtn.setAlpha(0.4f);

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

            String from = getResources().getString(R.string.horus_ai_email);
            String password = getResources().getString(R.string.horus_ai_password);
            String to = emailText.getText().toString();

            MailSender.send(from, password, to);

            // Launch confirm code activity
            Intent sendIntent = new Intent(this, ConfirmationCode.class);
            sendIntent.putExtra("Code", MailSender.getCode());
//            sendIntent.putExtra("LoggedEmail", MailSender.getLoggedGmail(this));
            startActivity(sendIntent);
            finish();

        }

        else if(v.getId()==R.id.forgot_password_layout_go_back_btn) {

            // Launch Login activity

            Toast.makeText(this, "Entao?", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(ForgotPassword.this,Login.class);
            startActivity(loginIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if(v.getId()==R.id.forgot_password_layout_close_cross_email) {

            // Clear username_text and cross.

            emailText.getText().clear();
            crossEmail.setVisibility(View.GONE);

        }

    }
}