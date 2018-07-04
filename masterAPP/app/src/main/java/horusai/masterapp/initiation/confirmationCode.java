package horusai.masterapp.initiation;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import horusai.masterapp.R;

public class confirmationCode extends AppCompatActivity implements View.OnClickListener,TextView.OnKeyListener,KeyboardView.OnKeyboardActionListener{

    private Button continueBtn;
    private ImageView spin;
    private EditText numberOne;
    private EditText numberTwo;
    private EditText numberThree;
    private EditText numberFour;
    private TextView errorText;
    private Keyboard telephoneKeyboard;
    private KeyboardView telephoneKeyboardView;
    private boolean keyActive = true;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open initiation_confirmation_code

        setContentView(R.layout.initiation_confirmation_code);

        // customize action bar
        myToolbar = findViewById(R.id.initiationConfirmationCode_Toolbar);

        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the Keyboard

        telephoneKeyboard = new Keyboard(confirmationCode.this,R.xml.keyboard);

        // Lookup the KeyboardView

        telephoneKeyboardView = findViewById(R.id.initiationConfirmationCode_KeyboardView);

        // Attach the keyboard to the view

        telephoneKeyboardView.setKeyboard(telephoneKeyboard);

        // Do not show the preview balloons

        telephoneKeyboardView.setPreviewEnabled(false);

        // Install the key handler

        telephoneKeyboardView.setOnKeyboardActionListener(this);

        // Set keyboard visible

        telephoneKeyboardView.setVisibility(View.VISIBLE);
        telephoneKeyboardView.setEnabled(true);

        // Initiate views

        continueBtn = findViewById(R.id.initiationConfirmationCode_ContinueBtn);
        spin = findViewById(R.id.initiationConfirmationCode_Spinner);
        numberOne = findViewById(R.id.initiationConfirmationCode_NumberOne);
        numberTwo = findViewById(R.id.initiationConfirmationCode_NumberTwo);
        numberThree = findViewById(R.id.initiationConfirmationCode_NumberThree);
        numberFour = findViewById(R.id.initiationConfirmationCode_NumberFour);
        errorText = findViewById(R.id.initiationConfirmationCode_ErrorText);

        // Disable default keyboard to open

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        // Make views respond to a click

        continueBtn.setOnClickListener(this);

        // Make edit_texts respond to writing and delete

        numberOne.addTextChangedListener(generalTextWatcher);
        numberTwo.addTextChangedListener(generalTextWatcher);
        numberThree.addTextChangedListener(generalTextWatcher);
        numberFour.addTextChangedListener(generalTextWatcher);

        // Make edit_text respond to only numbers

        numberOne.setOnKeyListener(this);
        numberTwo.setOnKeyListener(this);
        numberThree.setOnKeyListener(this);
        numberFour.setOnKeyListener(this);


        // Number_one_text focused

        numberOne.setEnabled(true);
        numberOne.requestFocus();

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        View focusCurrent = confirmationCode.this.getWindow().getCurrentFocus();

        focusCurrent.requestFocus();

        return true;

    }

        @Override
        public void onKey(int primaryCode, int[] keyCodes)
        {
            // Get the EditText Focused

            View focusCurrent = confirmationCode.this.getWindow().getCurrentFocus();

            if (!keyActive) return;

            EditText edittext = (EditText) focusCurrent;

             if( primaryCode==-5) { // When delete is pressed

                 if(edittext== numberTwo){

                     numberOne.getText().clear();
                     numberTwo.setEnabled(false);
                     numberOne.setEnabled(true);
                     numberOne.requestFocus();

                 }
                 else if(edittext== numberThree){

                     numberTwo.getText().clear();
                     numberThree.setEnabled(false);
                     numberTwo.setEnabled(true);
                     numberTwo.requestFocus();

                 }
                 else if(edittext== numberFour){

                     numberThree.getText().clear();
                     numberFour.setEnabled(false);
                     numberThree.setEnabled(true);
                     numberThree.requestFocus();
                 }

             }
             else if( primaryCode==-5000) {
                 assert edittext != null;
                 edittext.requestFocus();
             }
             else { // Insert character
                 assert edittext != null;
                 edittext.setText(Character.toString((char) primaryCode));
             }

        }

        @Override public void onPress(int arg0) {
        }

        @Override public void onRelease(int primaryCode) {
        }

        @Override public void onText(CharSequence text) {
        }

        @Override public void swipeDown() {
        }

        @Override public void swipeLeft() {
        }

        @Override public void swipeRight() {
        }

        @Override public void swipeUp() {
        }

    // Implement TextWatcher

    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            errorText.setVisibility(View.INVISIBLE);

            if (numberOne.getText().hashCode() == s.hashCode())
            {

                if (numberOne.getText().length()!=0) {
                    numberOne.setEnabled(false);
                    numberTwo.setEnabled(true);
                    numberTwo.requestFocus();
                }

            }

            else if (numberTwo.getText().hashCode() == s.hashCode())
            {
                if (numberTwo.getText().length()!=0) {
                    numberTwo.setEnabled(false);
                    numberThree.setEnabled(true);
                    numberThree.requestFocus();
                }
            }
            else if (numberThree.getText().hashCode() == s.hashCode())
            {
                if (numberThree.getText().length()!=0) {
                    numberThree.setEnabled(false);
                    numberFour.setEnabled(true);
                    numberFour.requestFocus();
                }
            }
            else if (numberFour.getText().hashCode() == s.hashCode())
            {
                if (numberFour.getText().length()!=0){

                    continueBtn.setEnabled(true);
                    continueBtn.performClick();
                    numberFour.setEnabled(false);

                }
                else if (numberFour.getText().length()==0){

                    continueBtn.setEnabled(false);

                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // Implement funcionalities after clicking buttons

    @Override
    public void onClick(View v) {

        if(v.getId()== continueBtn.getId()) {

            keyActive = false;

            // Disable other views

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // Start spinning loading

            spinvisibility(spin,View.VISIBLE, continueBtn,confirmationCode.this);


            String code  = numberOne.getText().toString() + numberTwo.getText().toString()
                            + numberThree.getText().toString() + numberFour.getText().toString();

                if(code.equals("1234")) { // If code written is correct

                spinvisibility(spin, View.INVISIBLE, continueBtn, confirmationCode.this);

                keyActive = true;

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                setResult(RESULT_OK);

                Intent registerIntent = new Intent(confirmationCode.this,register.class);
                startActivity(registerIntent);
                finish();

                }

                else{ // If code writen is wrong

                numberFour.getText().clear();
                numberThree.getText().clear();
                numberTwo.getText().clear();
                numberOne.getText().clear();

                errorText.setVisibility(View.VISIBLE);

                spinvisibility(spin, View.INVISIBLE, continueBtn, confirmationCode.this);

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                keyActive = true;

                numberOne.setEnabled(true);
                numberOne.requestFocus();

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
}
