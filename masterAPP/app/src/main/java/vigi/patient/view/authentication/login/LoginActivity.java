package vigi.patient.view.authentication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.Objects;
import vigi.patient.view.authentication.login.forgot_password.ForgotPasswordActivity;
import vigi.patient.view.authentication.registration.RegisterActivity;
import vigi.patient.view.patient.home.HomePatientActivity;
import vigi.patient.view.utils.dialog.ErrorDialog;
import vigi.patient.presenter.error.codes.FirebaseErrorCodes;
import vigi.patient.R;


import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private static String TAG = Activity.class.getName();

    private EditText passwordText;
    private EditText emailText;
    private FirebaseAuth authfire;
    private String errorText;
    private Button loginBtn;
    private TextView signUpBtn;
    private TextView lostPassBtn;
    private ImageView spin;
    private ScrollView scrollView;
    private LinearLayout background;
    private android.support.v7.widget.Toolbar myToolbar;
    private String errorCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout
        setContentView(R.layout.authentication_login);

        // Views
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_button);
        signUpBtn = findViewById(R.id.sign_up_button);
        lostPassBtn = findViewById(R.id.forgot_pass_button);
        spin = findViewById(R.id.spinner);
        scrollView = findViewById(R.id.scroll_view);
        myToolbar = findViewById(R.id.toolbar);
        background = findViewById(R.id.background);
        myToolbar = findViewById(R.id.toolbar);

        //remove scrollable indicator
        scrollView.setVerticalScrollBarEnabled(false);

        // Make views respond to a click
        signUpBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        lostPassBtn.setOnClickListener(this);

        // Customize action bar / toolbar
        setSupportActionBar(myToolbar);

        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Make layout respond to focus
        emailText.setOnFocusChangeListener(this);
        passwordText.setOnFocusChangeListener(this);

        // Setup input type of password programmatically to avoid getting bold
        passwordText.setTransformationMethod(new PasswordTransformationMethod());

        // Make password respond to enter
        passwordText.setOnEditorActionListener(this);
        passwordText.setOnKeyListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        // Try to Log In when enter is pressed in password text
        if (actionId == EditorInfo.IME_ACTION_GO) {
            loginBtn.performClick();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // Try to Log In when enter is pressed in password text from the enter button
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            switch (event.getAction()){
                case KeyEvent.ACTION_DOWN:
                    loginBtn.performClick();
                    break;
                case KeyEvent.ACTION_UP:
                    // nothing
            }
            return true;
        }
        return false;
    }

    // Implement click funcionalities
    @Override
    public void onClick(View v) {

            if(v.getId()== signUpBtn.getId()) { // Sign up
                Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signUpIntent);
                finish();

            } else if(v.getId()== lostPassBtn.getId()) { // Forgot password
                Intent forgotpasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotpasswordIntent);

            } else if(v.getId()== loginBtn.getId()) { // Log in
                // Disable movement and start spinning loader
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                spinVisibility(spin,View.VISIBLE, loginBtn,LoginActivity.this);
                // launch LoginActivity function
                loginAttempt();
            }
    }


    private void loginAttempt() {

        try {

            // Initialize Firebase auth
            authfire = FirebaseAuth.getInstance();

            // Get editText strings
            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            authfire.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Intent launchUserIntent = new Intent(LoginActivity.this, HomePatientActivity.class);
                        launchUserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchUserIntent);
                        finish();

                    } else { // if task not successful
                        try{
                            errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            FirebaseErrorCodes exceptionThrowed = new FirebaseErrorCodes();
                            errorText = exceptionThrowed.exceptionType(errorCode);
                            errorHandling(spin,background,loginBtn,LoginActivity.this,errorText);
                        } catch(ClassCastException e){
                            errorHandling(spin,background,loginBtn,LoginActivity.this,"Internet connection is not available.");
                        }
                    }
                }
            });
        } catch (IllegalArgumentException e){
            errorHandling(spin,background,loginBtn,LoginActivity.this,"Please enter a valid sign in, all fields are required.");
        }

    }

    // Focus handle
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && !passwordText.hasFocus() && !emailText.hasFocus()) {
            InputMethodManager input =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(input).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // Spinning handling
    private static void spinVisibility(View spinView, int visibility, Button btn, Context context) {
        if (visibility == View.INVISIBLE) {
            btn.setEnabled(true);
            spinView.clearAnimation();
            spinView.setVisibility(visibility);
            btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else if(visibility == View.VISIBLE) {
            btn.setEnabled(false);
            btn.setTextColor(context.getResources().getColor(R.color.transparent));
            spinView.setVisibility(visibility);
            spinView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely) );
        }
    }

    // Stop spinning loader, enable movement and launch error dialog
    private static void errorHandling(View spinView, View backgroundView, Button btn, Activity activity, String text) {
        backgroundView.performClick();
        spinVisibility(spinView, View.INVISIBLE, btn , activity);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ErrorDialog alert = new ErrorDialog();
        alert.showDialog(activity, text);
    }

    // Action when back arrow is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.not_movable,R.anim.slide_down);
                return true;
        }
        return false;
    }

    // Action when back navigation button is pressed
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
    }

}

