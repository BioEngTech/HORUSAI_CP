package horusai.masterapp.initiation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import horusai.masterapp.R;
import horusai.masterapp.utils.mailSender;

public class forgotPassword extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private Button send_btn;
    private EditText emailText;
    private ImageView spin;
    private TextView errorText;
    private boolean error=false;
    private Toolbar myToolbar;

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

        send_btn = findViewById(R.id.initiationForgotPassword_SendBtn);
        emailText = findViewById(R.id.initiationForgotPassword_Email);
        spin = findViewById(R.id.initiationForgotPassword_Spinner);
        errorText = findViewById(R.id.initiationForgotPassword_Error);

        // Make buttons and views respond to a click

        send_btn.setOnClickListener(this);

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

            send_btn.performClick();

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

                    send_btn.performClick();

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

        }
    };

    // Implement funcionalities after clicking buttons

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.initiationForgotPassword_SendBtn) {  // Send e-mail to user, and if he clicks the link on the e-mail the app opens the user's menu

            // Check if emailText is empty

            if(emailText.getText().length()==0){

                emailText.setBackgroundResource(R.drawable.rectangle_red_border);

                error=true;

                return;
            }

            // Disable other views

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // Start spinning loading

            spinvisibility(spin,View.VISIBLE,send_btn,forgotPassword.this);

            // Send e-mail to user

            String from = getResources().getString(R.string.horusai_email);
            String password = getResources().getString(R.string.horusai_password);
            String to = emailText.getText().toString();

            // TODO check if the email is correct

            // TODO - erro que aparece no Log quando E-mail nao é valido "com.sun.mail.smtp.SMTPAddressFailedException: 553 5.1.2 The recipient address <luis> is not a valid RFC-5321 address. c188-v6sm966566wma.31 - gsmtp"

            // TODO check if internet is working (throw exception)

            try{ // Send E-mail

                mailSender.send(from, password, to);

                spinvisibility(spin, View.INVISIBLE, send_btn, forgotPassword.this);

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Launch change password activity

                Intent sendIntent = new Intent(this, changePassword.class);
                sendIntent.putExtra("Code", mailSender.getCode());
                //sendIntent.putExtra("LoggedEmail", mailSender.getLoggedGmail(this));
                startActivityForResult(sendIntent,1);


            }catch (Exception e){ // If email is wrong or Net not working

                spinvisibility(spin, View.INVISIBLE, send_btn, forgotPassword.this);

                errorText.setVisibility(View.VISIBLE);

                errorText.setText("E-mail not valid");

                //errorText.setText("Internet connection failed");

                errorText.setTextColor(getResources().getColor(R.color.colorRed));

                emailText.setBackgroundResource(R.drawable.rectangle_red_border);

                error = true;

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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

    private static void spinvisibility(View spinView, int visibility, Button btn, Context context) {

        if (visibility == View.INVISIBLE) {
            btn.setEnabled(true); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
            spinView.clearAnimation();
            spinView.setVisibility(visibility);
            btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else if(visibility == View.VISIBLE) {
            btn.setEnabled(false); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
            btn.setTextColor(context.getResources().getColor(R.color.colorMain));
            spinView.setVisibility(visibility);
            spinView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely) );
        }
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