package horusai.masterapp.initiation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import horusai.masterapp.R;
import horusai.masterapp.user.main;
import horusai.masterapp.utils.errorDialog;
import horusai.masterapp.utils.firebaseExceptions;

public class register extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private EditText passwordText;
    private EditText emailText;
    private FirebaseAuth authfire;
    private String errorText;
    private Button registerBtn;
    private ImageView spin;
    private ImageView backBtn;
    private TextView termsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open initiation_register

        setContentView(R.layout.initiation_register);

        // Create objects

        emailText = findViewById(R.id.initiationRegister_Username);
        passwordText = findViewById(R.id.initiationRegister_Password);
        registerBtn = findViewById(R.id.initiationRegister_RegisterBtn);
        spin = findViewById(R.id.initiationRegister_Spinner);
        backBtn = findViewById(R.id.initiationRegister_BackBtn);
        termsAndConditions = findViewById(R.id.initiationRegister_TermsAndConditions);

        // Make buttons and views respond to a click

        registerBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        // Make layout respond to focus

        emailText.setOnFocusChangeListener(this);
        passwordText.setOnFocusChangeListener(this);

        // Make editText respond to enter

        passwordText.setOnEditorActionListener(this);
        passwordText.setOnKeyListener(this);

        String terms_conditions_text = "Terms and Conditions";

        SpannableString ss = new SpannableString(terms_conditions_text);

        ClickableSpan click_terms_conditions = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Toast.makeText(register.this,"Terms and Conditions are still missing.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(register.this.getResources().getColor(R.color.colorMain));
            }
        };

        ss.setSpan(click_terms_conditions,0,20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndConditions.setText(ss);
        termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());

        }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

            // Try to Log In when enter is pressed in password text

            if (actionId == EditorInfo.IME_ACTION_GO) {

                registerBtn.performClick();

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

                    registerBtn.performClick();

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

            if(v.getId()== backBtn.getId()) { // Back btn

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                InputMethodManager input =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            else if(v.getId()== registerBtn.getId()) { // Try to Log In

                // Disable other views

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Start spinning loading

                spinvisibility(spin,View.VISIBLE, registerBtn,register.this);

                // Start Sign In

                register_user();

            }
    }

    private void register_user() {

        try {

            // Initialize firebase auth

            authfire = FirebaseAuth.getInstance();

            // Get EditText Strings

            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            authfire.createUserWithEmailAndPassword(email,password).addOnCompleteListener(register.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    // if task not successful

                    if (task.isSuccessful()) {

                        Intent launchUserIntent = new Intent(register.this,main.class);
                        launchUserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchUserIntent);
                        finish();


                    } else {

                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        firebaseExceptions exceptionThrowed = new firebaseExceptions();

                        errorText = exceptionThrowed.exceptionType(errorCode);

                        spinvisibility(spin, View.INVISIBLE, registerBtn, register.this);

                        errorDialog alert = new errorDialog();
                        alert.showDialog(register.this, errorText);

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        emailText.requestFocus();

                    }

                }

            });

        }catch (IllegalArgumentException e){

            spinvisibility(spin, View.INVISIBLE, registerBtn, register.this);

            errorDialog alert = new errorDialog();
            alert.showDialog(register.this, "Please enter a valid sign up");

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

