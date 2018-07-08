package horusai.masterapp.initiation;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import horusai.masterapp.R;

public class changePassword extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private static String TAG = "changePassword";

    private ProgressBar fabProgressCircle;
    private FloatingActionButton confirmBtn;
    private TextView codeReceivedText;
    private TextView newPassword;
    private String expectedCode;
    private Toolbar myToolbar;
    private String loggedEmail;
    private TextView errorText;
    private boolean errorCode = false;
    private boolean errorPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getStringExtra("Code") != null) {
            expectedCode = getIntent().getStringExtra("Code");
            //loggedEmail = getIntent().getStringExtra("LoggedEmail");
        }

        // Open initiation_change_password

        setContentView(R.layout.initiation_change_password);

        // Customize action bar

        myToolbar = findViewById(R.id.initiationChangePassword_Toolbar);

        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        errorText = findViewById(R.id.initiationChangePassword_Error);
        codeReceivedText = findViewById(R.id.initiationChangePassword_CodeReceived);
        newPassword = findViewById(R.id.initiationChangePassword_NewPassword);
        confirmBtn = findViewById(R.id.initiationChangePassword_ConfirmBtn);
        fabProgressCircle = findViewById(R.id.initiationChangePassword_ProgressCircle);

        codeReceivedText.addTextChangedListener(generalTextWatcher);

        newPassword.addTextChangedListener(generalTextWatcher);
        newPassword.setOnEditorActionListener(this);
        newPassword.setOnKeyListener(this);

        confirmBtn.setOnClickListener(this);

        confirmBtn.setEnabled(false);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

        // Try to Log In when enter is pressed in password text

        if (actionId == EditorInfo.IME_ACTION_GO) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(newPassword, InputMethodManager.SHOW_IMPLICIT);


            if (confirmBtn.isEnabled()){

                confirmBtn.performClick();

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

                    if (confirmBtn.isEnabled()){

                        confirmBtn.performClick();

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

            if (errorCode && codeReceivedText.getText().hashCode() == s.hashCode()) {

                errorText.setVisibility(View.INVISIBLE);

                codeReceivedText.setBackgroundResource(R.drawable.rectangle_main_border);

                errorCode = false;

            }

            else if (errorPass && newPassword.getText().hashCode() == s.hashCode()) {

                errorText.setVisibility(View.INVISIBLE);

                newPassword.setBackgroundResource(R.drawable.rectangle_main_border);

                errorPass = false;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (codeReceivedText.getText().length()!=0 && newPassword.getText().length()!=0 ){
                confirmBtn.setEnabled(true);
                confirmBtn.setBackgroundTintList(changePassword.this.getResources().getColorStateList(R.color.colorMain));
            }
            else{

                confirmBtn.setEnabled(false);
                confirmBtn.setBackgroundTintList(changePassword.this.getResources().getColorStateList(R.color.colorGrayNormal));
            }

        }
    };

    @Override
    public void onClick(View v) {

        if(v.getId()== confirmBtn.getId()) {

            // Check if error still there

            if(errorText.getVisibility()==View.VISIBLE){

                return;
            }

            // Disable other views

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // Start spinning loading

            confirmBtn.setImageResource(0);
            fabProgressCircle.setVisibility(View.VISIBLE);

            // Handler 0.5 sec is just to show the spinner at least 1 second, to look cool :)

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                        if (!codeReceivedText.getText().toString().equals(expectedCode)) { // Code received is wrong

                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText("Wrong code");
                            errorText.setTextColor(getResources().getColor(R.color.colorRed));

                            codeReceivedText.setBackgroundResource(R.drawable.rectangle_red_border);

                            errorCode = true;

                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            fabProgressCircle.setVisibility(View.GONE);
                            confirmBtn.setImageResource(R.drawable.continue_arrow);

                            codeReceivedText.requestFocus();

                        }

                        // TODO verificar se palavra passe funciona para o firebase

                        else if (false){ // Password Invalid

                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText("Password not valid");
                            errorText.setTextColor(getResources().getColor(R.color.colorRed));

                            newPassword.setBackgroundResource(R.drawable.rectangle_red_border);

                            errorPass = true;

                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            fabProgressCircle.setVisibility(View.GONE);
                            confirmBtn.setImageResource(R.drawable.continue_arrow);

                            newPassword.requestFocus();

                        }

                        else{

                            // TODO adicionar palavra passe ao firebase

                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            fabProgressCircle.setVisibility(View.GONE);
                            confirmBtn.setImageResource(R.drawable.continue_arrow);

                            setResult(RESULT_OK);

                            finish();

                        }

                }
            }, 500);

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

}

