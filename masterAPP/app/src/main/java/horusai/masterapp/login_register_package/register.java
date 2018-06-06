package horusai.masterapp.login_register_package;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
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

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import horusai.masterapp.R;

public class register extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener {

    private TextView terms_and_conditions;
    private ImageView cross_pass;
    private EditText password_text;
    private ImageView cross_email;
    private EditText email_text;
    private CircularProgressButton register_btn;
    private Button signin_btn;
    private FirebaseAuth authfire;
    private String error_text;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open register_layout

        setContentView(R.layout.register_layout);

        // Create objects

        cross_email = findViewById(R.id.register_layout_close_cross_email);
        cross_pass = findViewById(R.id.register_layout_close_cross_pass);
        email_text = findViewById(R.id.register_layout_email);
        password_text = findViewById(R.id.register_layout_password);
        register_btn = findViewById(R.id.register_layout_register_btn);
        signin_btn = findViewById(R.id.register_layout_sign_in_btn);
        terms_and_conditions = findViewById(R.id.register_layout_terms);

        // Make buttons and views respond to a click

        signin_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        cross_email.setOnClickListener(this);
        cross_pass.setOnClickListener(this);
        terms_and_conditions.setOnClickListener(this);

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

        register_btn.setEnabled(false);
        register_btn.setAlpha(0.4f);

        // Set terms and conditions links!

        String terms_conditions_text = "By registering, you agree to the Terms and Conditions.";

        SpannableString ss = new SpannableString(terms_conditions_text);

        ClickableSpan click_terms_conditions = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Toast.makeText(register.this,"Terms and Conditions are still missing.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorblue));
            }
        };

        ss.setSpan(click_terms_conditions,33,53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        terms_and_conditions.setText(ss);
        terms_and_conditions.setMovementMethod(LinkMovementMethod.getInstance());
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

            // Change button color/state

            if (result==-1) {

                signin_btn.setEnabled(true);
                register_btn.setBackground(getResources().getDrawable(R.drawable.rounded_blue));
                register_btn.setText(getResources().getString(R.string.try_again_text));
                register_btn.setTextSize(20);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            // Change register btn availability

            if (email_text.getText().length()!=0 && password_text.getText().length()!=0){

                register_btn.setEnabled(true);
                register_btn.setAlpha(1f);

            }

            else{

                register_btn.setEnabled(false);
                register_btn.setAlpha(0.4f);

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

        if(v.getId()==R.id.register_layout_sign_in_btn) {

            // Launch login activity

            Intent loginIntent = new Intent(register.this,login.class);
            startActivity(loginIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if(v.getId()==R.id.register_layout_register_btn) {

            // Disable other texts/buttons

            email_text.setEnabled(false);
            password_text.setEnabled(false);
            signin_btn.setEnabled(false);

            // Start animation circle

            register_btn.startAnimation();

            // Initialize firebase auth

            authfire = FirebaseAuth.getInstance();

            register_user();

            email_text.setEnabled(true);
            password_text.setEnabled(true);

        }

        else if(v.getId()==R.id.register_layout_close_cross_email) {

            // Clear username_text and cross.

            email_text.getText().clear();
            cross_email.setVisibility(View.GONE);

        }

        else if(v.getId()==R.id.register_layout_close_cross_pass) {

            // Clear password_text and cross.

            password_text.getText().clear();
            cross_pass.setVisibility(View.GONE);

        }

    }

    private void register_user(){

        String email =  email_text.getText().toString().trim();
        String password = password_text.getText().toString().trim();

        authfire.createUserWithEmailAndPassword(email,password).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // if task not successful

                if (task.isSuccessful()) {

                    register_btn.revertAnimation(new OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {

                            register_btn.setTextSize(20);
                            register_btn.setText(getResources().getString(R.string.register_sucess));
                        }
                    });
                }
                else {

                    result=-1;

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    if (errorCode.equals("ERROR_INVALID_CUSTOM_TOKEN")) {
                        error_text = "The custom token format is incorrect. Please check the documentation.";
                    }

                    else if (errorCode.equals("ERROR_CUSTOM_TOKEN_MISMATCH")){
                        error_text = "The custom token corresponds to a different audience.";

                    }

                    else if (errorCode.equals("ERROR_INVALID_CREDENTIAL")){

                        error_text = "The supplied auth credential is malformed or has expired.";

                    }

                    else if (errorCode.equals("ERROR_INVALID_EMAIL")){

                            error_text = "The email address is badly formatted.";

                    }
                    else if (errorCode.equals("ERROR_WRONG_PASSWORD")){

                        error_text = "The password is invalid.";

                    }
                    else if (errorCode.equals("ERROR_USER_MISMATCH")){

                        error_text = "The supplied credentials do not correspond to the previously signed in user.";

                    }
                    else if (errorCode.equals("ERROR_REQUIRES_RECENT_LOGIN")){

                        error_text = "This operation is sensitive and requires recent authentication. Log in again before retrying this request.";

                    }
                    else if (errorCode.equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")){

                        error_text = "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.";

                    }
                    else if (errorCode.equals("ERROR_CREDENTIAL_ALREADY_IN_USE")){

                        error_text = "This credential is already associated with a different user account.";

                    }
                    else if (errorCode.equals("ERROR_USER_DISABLED")){

                        error_text = "The user account has been disabled by an administrator.";

                    }
                    else if (errorCode.equals("ERROR_USER_TOKEN_EXPIRED")){

                        error_text = "The user's credential is no longer valid. The user must sign in again.";

                    }
                    else if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")){

                        error_text = "The email address is already in use by another account.";

                    }
                    else if (errorCode.equals("ERROR_USER_NOT_FOUND")){

                        error_text = "There is no user record corresponding to this identifier. The user may have been deleted.";


                    }
                    else if (errorCode.equals("ERROR_OPERATION_NOT_ALLOWED")){

                        error_text = "This operation is not allowed. You must enable this service in the console.";

                    }
                    else if (errorCode.equals("ERROR_WEAK_PASSWORD")){

                        error_text = "The given password is invalid.";

                    }
                    else if (errorCode.equals("ERROR_INVALID_USER_TOKEN")){

                        error_text = "The user's credential is no longer valid. The user must sign in again.";

                    }

                    register_btn.revertAnimation(new OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {

                            register_btn.setTextSize(15);
                            register_btn.setBackground(getResources().getDrawable(R.drawable.rounded_red));
                            register_btn.setText(error_text);
                        }
                    });
                }

            }


        });

    }
}
