package horusai.masterapp.initiation;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import horusai.masterapp.R;

public class telephone extends AppCompatActivity implements View.OnClickListener,TextView.OnKeyListener,KeyboardView.OnKeyboardActionListener{

    private ProgressBar fabProgressCircle;
    private FloatingActionButton continueBtn;
    private EditText telephoneNumberText;
    private TextView telephoneError;
    private View telephoneTextBackgroundBorder;
    private Keyboard telephoneKeyboard;
    private KeyboardView telephoneKeyboardView;
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
        fabProgressCircle = findViewById(R.id.initiationTelephone_ProgressCircle);

        // Make buttons and views respond to a click

        continueBtn.setOnClickListener(this);

        // Make button disable at beggining

        continueBtn.setEnabled(false);

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

            editable.insert(start, Character.toString((char) primaryCode));

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

            if (telephoneNumberText.getText().length()!=0){
                continueBtn.setEnabled(true);
                continueBtn.setBackgroundTintList(telephone.this.getResources().getColorStateList(R.color.colorMain));
            }
            else{

                continueBtn.setEnabled(false);
                continueBtn.setBackgroundTintList(telephone.this.getResources().getColorStateList(R.color.colorGrayNormal));

            }

        }
    };

    // Implement funcionalities after clicking buttons

    @Override
    public void onClick(View v) {

        if(v.getId()== continueBtn.getId()) {

            keyActive = false;

            // Check if emailText is still wrong

            if(telephoneError.getVisibility()==View.VISIBLE){

                keyActive = true;

                return;
            }

            // Disable other views

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            continueBtn.setImageResource(0);
            fabProgressCircle.setVisibility(View.VISIBLE);

            // Handler 0.5 sec is just to show the spinner at least 1 second, to look cool :)

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (telephoneNumberText.getText().toString().trim().equals("91")){ // if number valid

                        keyActive = true;

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        fabProgressCircle.setVisibility(View.GONE);
                        continueBtn.setImageResource(R.drawable.continue_arrow);

                        // Launch confirm code activity

                        Intent sendIntent = new Intent(telephone.this,confirmationCode.class);
                        startActivityForResult(sendIntent,11);

                    }

                    else{ // if number not valid

                        telephoneTextBackgroundBorder.setBackgroundResource(R.drawable.rectangle_red_border);
                        telephoneError.setVisibility(View.VISIBLE);

                        keyActive = true;
                        error = true;

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        fabProgressCircle.setVisibility(View.GONE);
                        continueBtn.setImageResource(R.drawable.continue_arrow);

                        telephoneNumberText.requestFocus();

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
