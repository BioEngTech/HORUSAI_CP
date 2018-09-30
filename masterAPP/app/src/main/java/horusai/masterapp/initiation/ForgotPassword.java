package horusai.masterapp.initiation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import horusai.masterapp.R;
import horusai.masterapp.exception.NoNetworkException;
import horusai.masterapp.utils.mail.GeneralMailValidator;
import horusai.masterapp.utils.mail.MailSender;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private ProgressBar fabProgressCircle;
    private FloatingActionButton continueBtn;
    private EditText emailText;
    private TextView errorText;
    private boolean error=false;
    private Toolbar myToolbar;
    private MailSender mailSender = new MailSender();

    private static String TAG = "forgotPasswordClass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open initiation_forgot_password

        setContentView(R.layout.initiation_forgot_password);

        // Customize action bar

        myToolbar = findViewById(R.id.initiationForgotPassword_Toolbar);

        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create objects

        continueBtn = findViewById(R.id.initiationForgotPassword_ContinueBtn);
        emailText = findViewById(R.id.initiationForgotPassword_Email);
        errorText = findViewById(R.id.initiationForgotPassword_Error);
        fabProgressCircle = findViewById(R.id.initiationForgotPassword_ProgressCircle);

        // Make buttons and views respond to a click

        continueBtn.setOnClickListener(this);

        // Diable button at beggining

        continueBtn.setEnabled(false);

        // Make editText respond to enter

        emailText.addTextChangedListener(generalTextWatcher);
        emailText.setOnEditorActionListener(this);
        emailText.setOnKeyListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

        // Try to Log In when enter is pressed in password text

        if (actionId == EditorInfo.IME_ACTION_GO) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(emailText, InputMethodManager.SHOW_IMPLICIT);


            if (continueBtn.isEnabled()){

                continueBtn.performClick();

            }

            return true;
        }
        return false;

    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // Try to Log In when enter is pressed in password text from the enter button on

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            switch (event.getAction()){

                case KeyEvent.ACTION_DOWN:

                    if (continueBtn.isEnabled()){

                        continueBtn.performClick();

                    }

                    break;

                case KeyEvent.ACTION_UP:

                    // nothing

                    break;

            }

            return true;

        }

        return false;
    }

    // Implement TextWatcher

    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (error) {

                errorText.setVisibility(View.INVISIBLE);

                emailText.setBackgroundResource(R.drawable.rectangle_main_border);

                error = false;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (emailText.getText().length()!=0){
                continueBtn.setEnabled(true);
                continueBtn.setBackgroundTintList(ForgotPassword.this.getResources().getColorStateList(R.color.colorMain));
            }
            else{

                continueBtn.setEnabled(false);
                continueBtn.setBackgroundTintList(ForgotPassword.this.getResources().getColorStateList(R.color.colorGrayNormal));

            }

        }
    };

    // Implement funcionalities after clicking buttons

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.initiationForgotPassword_ContinueBtn) {  // Send e-mail to user, and if he clicks the link on the e-mail the app opens the user's menu

            // Check if emailText is still wrong

            if(errorText.getVisibility()==View.VISIBLE){

                return;
            }

            // Disable other views

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // Start spinning loading

            continueBtn.setImageResource(0);
            fabProgressCircle.setVisibility(View.VISIBLE);

            // Send e-mail to user

            final String from = getResources().getString(R.string.horusai_email);
            final String password = getResources().getString(R.string.horusai_password);
            final String to = emailText.getText().toString();

            try { // Send E-mail

                if (!GeneralMailValidator.isEmailValid(to)) throw new Exception();

                final boolean success = mailSender.send(from, password, to);

                fabProgressCircle.setVisibility(View.GONE);
                continueBtn.setImageResource(R.drawable.continue_arrow);

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if (!success) throw new NoNetworkException("Please connect to internet!");

                // Launch change password activity

                Intent sendIntent = new Intent(ForgotPassword.this, ChangePassword.class);
                sendIntent.putExtra("Code", MailSender.getCode());
                //sendIntent.putExtra("LoggedEmail", MailSender.getLoggedGmail(this));
                startActivityForResult(sendIntent,1);

            } catch (Exception e){ // If email is wrong or Net not working

                Log.d(TAG,e.toString());

                errorText.setVisibility(View.VISIBLE);

                if ("Please connect to internet!".equals(e.getMessage())){
                    errorText.setText("Internet connection failed");
                }

                errorText.setTextColor(getResources().getColor(R.color.colorRed));

                emailText.setBackgroundResource(R.drawable.rectangle_red_border);

                error = true;

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                fabProgressCircle.setVisibility(View.GONE);
                continueBtn.setImageResource(R.drawable.continue_arrow);

                emailText.requestFocus();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                setResult(RESULT_OK, null);

                this.finish();
            }
        }
    }

}
