package vigi.patient.initiation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.Calendar;

import vigi.patient.R;
import vigi.patient.user.main;
import vigi.patient.utils.errorDialog;
import vigi.patient.utils.exceptions.firebase;

@SuppressWarnings("FieldCanBeLocal")
public class register extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private static String TAG = "registerClass";

    private EditText passwordText;
    private EditText emailText;
    private FirebaseAuth authfire;
    private String errorText;
    private Button registerBtn;
    private ImageView spin;
    private TextView termsAndConditions;
    private ScrollView scrollView;
    private Toolbar myToolbar;
    private EditText birthday;
    private EditText name;
    private EditText phone;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String errorCode = "";
    private LinearLayout background;
    private AppCompatImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout

        setContentView(R.layout.initiation_register);

        // Objects

        emailText = findViewById(R.id.initiationRegister_email);
        passwordText = findViewById(R.id.initiationRegister_Password);
        name = findViewById(R.id.initiationRegister_name);
        phone = findViewById(R.id.initiationRegister_phoneNumber);
        registerBtn = findViewById(R.id.initiationRegister_registerButton);
        spin = findViewById(R.id.initiationRegister_spinner);
        termsAndConditions = findViewById(R.id.initiationRegister_termsAndConditions);
        scrollView = findViewById(R.id.initiationRegister_scrollview);
        birthday = findViewById(R.id.initiationRegister_birthday);
        myToolbar = findViewById(R.id.initiationRegister_toolbar);
        photo = findViewById(R.id.initiationRegister_ProfilePhoto);
        background = findViewById(R.id.initiationRegister_background);

        //remove scrollable indicator

        scrollView.setVerticalScrollBarEnabled(false);

        // Customize action bar / toolbar

        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Make buttons and views respond to a click

        photo.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        // Make layout respond to focus

        name.setOnFocusChangeListener(this);
        phone.setOnFocusChangeListener(this);
        emailText.setOnFocusChangeListener(this);
        passwordText.setOnFocusChangeListener(this);

        // Make editText respond to enter
        //name.setOnKeyListener(this);
        passwordText.setOnEditorActionListener(this);
        passwordText.setOnKeyListener(this);

        // Setup input type of password programmatically to avoid getting bold

        passwordText.setTransformationMethod(new PasswordTransformationMethod());

        // Activate Birthday Dialog

        birthday.setKeyListener(null);

        birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!birthday.hasFocus()){
                    return;
                }

                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(register.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,year,month,day);

                long hundred_ten_years = 1000*60*60*24*39600L;

                dialog.getDatePicker().setMinDate(cal.getTimeInMillis() - hundred_ten_years);

                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                dialog.setCancelable(false);

                dialog.show();

                emailText.requestFocus();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;

                String date = dayOfMonth + "/" + month + "/" + year;

                birthday.setText(date);

                emailText.requestFocus();

            }
        };

        // Set an underline to the terms and conditions

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
                ds.setColor(register.this.getResources().getColor(R.color.colorWhite));
            }
        };

        ss.setSpan(click_terms_conditions,0,terms_conditions_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndConditions.setText(ss);
        termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());


        // TODO when user selects a country flag for his phone number, automatically change the hint on the right side according to the format of the number in that country

        }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

            // Try to Register when enter is pressed in password text

            if (actionId == EditorInfo.IME_ACTION_GO) {

                registerBtn.performClick();

            return true;
        }
        return false;

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // Try to Register when enter is pressed in password text from the enter button on

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            switch (event.getAction()){

                case KeyEvent.ACTION_DOWN:

                    if (name.hasFocus()){

                    birthday.performClick();

                    }

                    else if (passwordText.hasFocus()){

                        registerBtn.performClick();

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

    // Implement click funcionalities

    @Override
    public void onClick(View v) {

            if(v.getId()== registerBtn.getId()) { // Try to Log In

                // Disable movement and start spinning loader

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                spinVisibility(spin,View.VISIBLE, registerBtn,register.this);

                // Check if fields are filled

                if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || birthday.getText().toString().isEmpty()){

                    errorHandling(spin, background,registerBtn,register.this,"Please enter a valid sign up, all fields are required.");

                    return;
                }

                else if (false){

                    // TODO check if phone number is plausible otherwise return error

                    errorHandling(spin, background,registerBtn,register.this,"Please enter a valid phone number.");

                    return;

                }

                else if (false){

                    // TODO check if birthday is plausible otherwise return error

                    errorHandling(spin, background,registerBtn,register.this,"Too young.");

                    return;

                }

                // launch register function

                registerAttempt();

            }

            else if(v.getId()== photo.getId()) { // Try to Log In

                // TODO ask user to select a photo or to take one

                photo.setBackgroundResource(0); // to delete the drawable that was inside the circle

                // TODO upload the photo to the inside of the circle like example below

                photo.setImageResource(R.drawable.image_photo);

            }
    }

    private void registerAttempt() {


        try {

            // Initialize firebase auth

            authfire = FirebaseAuth.getInstance();

            // Get EditText Strings

            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            authfire.createUserWithEmailAndPassword(email,password).addOnCompleteListener(register.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        // TODO - mail and password were created , but the other elements also need to be added to firebase

                        Intent launchUserIntent = new Intent(register.this,main.class);
                        launchUserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchUserIntent);
                        finish();

                    } else { // if task not successful

                        try{

                            errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            firebase exceptionThrowed = new firebase();

                            errorText = exceptionThrowed.exceptionType(errorCode);

                            errorHandling(spin, background,registerBtn,register.this,errorText);


                        }catch(ClassCastException e){

                            errorHandling(spin, background,registerBtn,register.this,"Internet connection is not available.");

                        }

                    }

                }

            });

        }catch (IllegalArgumentException e){

            errorHandling(spin, background,registerBtn,register.this,"Please enter a valid sign up, all fields are required.");

        }

    }

    // Focus handle

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus && !name.hasFocus() && !phone.hasFocus() && !passwordText.hasFocus() && !emailText.hasFocus()) {
            InputMethodManager input =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }

    // Spinning handling

    private static void spinVisibility(View spinView, int visibility, Button btn, Context context) {

        if (visibility == View.INVISIBLE) {
            btn.setEnabled(true); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
            spinView.clearAnimation();
            spinView.setVisibility(visibility);
            btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else if(visibility == View.VISIBLE) {
            btn.setEnabled(false); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
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
        errorDialog alert = new errorDialog();
        alert.showDialog(activity, text);
    }

    // Action when back arrow is pressed

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.not_movable,R.anim.slide_down);
                return true;
        }

        return false;
    }

    // Action when back navigation button is pressed

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
    }

}

