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

import horusai.masterapp.R;

public class telephone extends AppCompatActivity implements View.OnClickListener,TextView.OnKeyListener,KeyboardView.OnKeyboardActionListener{

    private Button continueBtn;
    private EditText telephoneNumberText;
    private TextView telephoneError;
    private View telephoneTextBackgroundBorder;
    private Keyboard telephoneKeyboard;
    private KeyboardView telephoneKeyboardView;
    private ImageView spin;
    private boolean error = false;
    private boolean keyActive = true;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout

        setContentView(R.layout.initiation_telephone);

        // Customize action bar

        myToolbar = findViewById(R.id.initiationTelephone_Toolbar);

        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the Keyboard

        telephoneKeyboard = new Keyboard(telephone.this,R.xml.keyboard);

        // Lookup the KeyboardView

        telephoneKeyboardView = findViewById(R.id.initiationTelephone_KeyboardView);

        // Attach the keyboard to the view

        telephoneKeyboardView.setKeyboard(telephoneKeyboard);

        // Do not show the preview balloons

        telephoneKeyboardView.setPreviewEnabled(false);

        // Install the key handler

        telephoneKeyboardView.setOnKeyboardActionListener(this);

        // Set keyboard visible

        telephoneKeyboardView.setVisibility(View.VISIBLE);
        telephoneKeyboardView.setEnabled(true);

        // Create objects

        continueBtn = findViewById(R.id.initiationTelephone_ContinueBtn);
        telephoneNumberText = findViewById(R.id.initiationTelephone_Number);
        telephoneError = findViewById(R.id.initiationTelephone_Error);
        telephoneTextBackgroundBorder = findViewById(R.id.initiationTelephone_NumberBorder);
        spin = findViewById(R.id.initiationTelephone_Spinner);

        // Make buttons and views respond to a click

        continueBtn.setOnClickListener(this);

        // Disable default keyboard to open

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        // Make edit_text respond to only numbers

        telephoneNumberText.setOnKeyListener(this);

        // Make edit_text respond to writing

        telephoneNumberText.addTextChangedListener(generalTextWatcher);

        // Make telephoneNumberText focused

        telephoneNumberText.requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        telephoneNumberText.requestFocus();

        return true;

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes)
    {
        // Get the EditText Focused

        View focusCurrent = telephone.this.getWindow().getCurrentFocus();

        if (!keyActive) return;

        EditText edittext = (EditText) focusCurrent;

        assert edittext != null;
        Editable editable = edittext.getText();

        int start = edittext.getSelectionStart();

        if( primaryCode==-5) { // When delete is pressed

            if( editable!=null && start>0 ) editable.delete(start - 1, start);
        }
        else if( primaryCode==-5000) { // When null character is pressed

            edittext.requestFocus();
        }
        else{

            if (telephoneNumberText.getText().length() == 15){

                telephoneTextBackgroundBorder.setBackgroundResource(R.drawable.rectangle_red_border);
                telephoneError.setVisibility(View.VISIBLE);
                telephoneError.setText(R.string.Max_length_possible_text);

                error = true;

            }
            else{
                editable.insert(start, Character.toString((char) primaryCode));
            }

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

            if (error) {

                telephoneTextBackgroundBorder.setBackgroundResource(R.drawable.rectangle_main_border);
                telephoneError.setVisibility(View.INVISIBLE);

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

        if(v.getId()== continueBtn.getId()) {

            keyActive = false;

            if(telephoneNumberText.getText().length()==0){

                telephoneTextBackgroundBorder.setBackgroundResource(R.drawable.rectangle_red_border);

                error=true;

                keyActive = true;

                return;
            }

            // Disable other views

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // Start spinning loading

            spinvisibility(spin,View.VISIBLE, continueBtn,telephone.this);


            if (telephoneNumberText.getText().toString().trim().equals("91")){ // if number valid

                spinvisibility(spin, View.INVISIBLE, continueBtn, telephone.this);

                keyActive = true;

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Launch confirm code activity

                Intent sendIntent = new Intent(telephone.this,confirmationCode.class);
                startActivityForResult(sendIntent,11);

                }

                else{ // if number not valid

                spinvisibility(spin, View.INVISIBLE, continueBtn, telephone.this);

                telephoneTextBackgroundBorder.setBackgroundResource(R.drawable.rectangle_red_border);
                telephoneError.setVisibility(View.VISIBLE);
                telephoneError.setText(R.string.number_invalid_text);

                keyActive = true;
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

        if (requestCode == 11) { // Coming from activity confirmation code

            if (resultCode == RESULT_OK) {

                setResult(RESULT_OK);

                this.finish();

            }
        }
    }
}
