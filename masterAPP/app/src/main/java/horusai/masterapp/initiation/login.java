package horusai.masterapp.initiation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import horusai.masterapp.R;
import horusai.masterapp.utils.errorDialog;
import horusai.masterapp.utils.firebaseExceptions;

public class login extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private EditText passwordText;
    private EditText emailText;
    private FirebaseAuth authfire;
    private String errorText;
    private Button loginBtn;
    private TextView signUpBtn;
    private TextView lostPassBtn;
    private ImageView spin;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open initiation_login

        setContentView(R.layout.initiation_login);

        // Create objects

        emailText = findViewById(R.id.initiationLogin_Username);
        passwordText = findViewById(R.id.initiationLogin_Password);
        loginBtn = findViewById(R.id.initiationLogin_LoginBtn);
        signUpBtn = findViewById(R.id.initiationLogin_SignUpBtn);
        lostPassBtn = findViewById(R.id.initiationLogin_ForgotPass);
        spin = findViewById(R.id.initiationLogin_Spinner);
        backBtn = findViewById(R.id.initiationLogin_BackBtn);

        // Make buttons and views respond to a click

        signUpBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        lostPassBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        // Make layout respond to focus

        emailText.setOnFocusChangeListener(this);
        passwordText.setOnFocusChangeListener(this);

        // Make editText respond to enter

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

        // Try to Log In when enter is pressed in password text from the enter button on

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            switch (event.getAction()){

                case KeyEvent.ACTION_DOWN:

                    loginBtn.performClick();

                    break;

                case KeyEvent.ACTION_UP:

                    // nothing

                    break;

            }

            return true;

        }

        return false;
    }

    // Implement funcionalities after clicking buttons

    @Override
    public void onClick(View v) {

            if(v.getId()== signUpBtn.getId()) { // Implement unfocus

                Intent configureTypeUserIntent = new Intent(login.this,configureTypeUser.class);
                startActivity(configureTypeUserIntent);
                finish();
                overridePendingTransition(R.anim.slide_up,R.anim.not_movable);            }

            else if(v.getId()== lostPassBtn.getId()) { // Forgot Password

                Intent forgotpasswordIntent = new Intent(login.this,forgotPassword.class);
                startActivity(forgotpasswordIntent);
            }

            else if(v.getId()== backBtn.getId()) { // Back btn

                    finish();
                    overridePendingTransition(R.anim.not_movable,R.anim.slide_down);
            }

            else if(v.getId()== loginBtn.getId()) { // Try to Log In

                // Disable other views

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Start spinning loading

                spinvisibility(spin,View.VISIBLE, loginBtn,login.this);

                // Start Sign In

                login_user();

            }
    }

    private void login_user() {

        try {

            // Initialize firebase auth

            authfire = FirebaseAuth.getInstance();

            // Get EditText Strings

            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            authfire.signInWithEmailAndPassword(email, password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    // if task not successful

                    if (task.isSuccessful()) {


                    } else {

                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        firebaseExceptions exceptionThrowed = new firebaseExceptions();

                        errorText = exceptionThrowed.exceptionType(errorCode);

                        spinvisibility(spin, View.INVISIBLE, loginBtn, login.this);

                        errorDialog alert = new errorDialog();
                        alert.showDialog(login.this, errorText);

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        emailText.requestFocus();

                    }

                }

            });

        }catch (IllegalArgumentException e){

            spinvisibility(spin, View.INVISIBLE, loginBtn, login.this);

            errorDialog alert = new errorDialog();
            alert.showDialog(login.this, "Please enter a valid sign in");

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            emailText.requestFocus();

        }

    }

    // Implement unfocus

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus && !passwordText.hasFocus()) {
            InputMethodManager input =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(v.getWindowToken(), 0);

        }
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

}

