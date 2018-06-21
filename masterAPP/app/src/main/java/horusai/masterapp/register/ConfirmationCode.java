package horusai.masterapp.register;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import horusai.masterapp.R;

public class ConfirmationCode extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private Button continueBtn;
    private Button goBackBtn;
    private EditText confirmationCodeText;
    private String expectedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getStringExtra("Code") != null) {
            expectedCode = getIntent().getStringExtra("Code");
        }

        // Open login_layout

        setContentView(R.layout.confirmation_code_layout);

        // Create objects

        continueBtn = findViewById(R.id.confirmation_layout_continue_btn);
        goBackBtn = findViewById(R.id.confirmation_layout_go_back_btn);
        confirmationCodeText = findViewById(R.id.confirmation_layout_code);


        // Make buttons and views respond to a click

        goBackBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);

        // Make layout respond to focus

        confirmationCodeText.setOnFocusChangeListener(this);

    }

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

        if(v.getId()==R.id.confirmation_layout_continue_btn) {

            if (!confirmationCodeText.getText().toString().equals(expectedCode)) {
                Toast.makeText(this, "Are you sure that is the code?", Toast.LENGTH_SHORT).show();
                return;
            }

            // Launch registering activity
            Toast.makeText(this, "Success bro", Toast.LENGTH_SHORT).show();
            Intent registerIntent = new Intent(ConfirmationCode.this,Register.class);
            startActivity(registerIntent);
            finish();
        }

        else if(v.getId()==R.id.confirmation_layout_go_back_btn) {

            // Launch phone activity

            Intent phoneIntent = new Intent(ConfirmationCode.this,Email.class);
            startActivity(phoneIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
