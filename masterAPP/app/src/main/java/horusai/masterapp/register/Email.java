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

import java.util.Random;

import horusai.masterapp.R;
import horusai.masterapp.email.GmailSender;

public class Email extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private Button sendBtn;
    private Button signInBtn;
    private EditText emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open login_layout

        setContentView(R.layout.email_layout);

        // Create objects

        sendBtn = findViewById(R.id.email_layout_send_btn);
        signInBtn = findViewById(R.id.email_layout_sign_in_btn);
        emailText = findViewById(R.id.email_layout_text);

        // Make buttons and views respond to a click

        signInBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);

        // Make layout respond to focus

        emailText.setOnFocusChangeListener(this);

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

        if(v.getId()==R.id.email_layout_send_btn) {

            String to = emailText.getText().toString();
            String from = "jlbgsousa@gmail.com";
            try {

                GmailSender sender = new GmailSender(from, "passwordMock");
                String alphaNumericDictionary = "ABCDEFGHIJKLMNOPQRSTUVXWYZ1234567890";
                int passwordLength = 8;
                StringBuilder stringBuilder = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < passwordLength; i++) {
                    stringBuilder.append(alphaNumericDictionary.charAt(random.nextInt(alphaNumericDictionary.length())));
                }
                sender.sendMail("Generated Vigi Code",
                        stringBuilder.toString(),
                        from,
                        to);

                // Launch confirm code activity
                Intent sendIntent = new Intent(Email.this,ConfirmationCode.class);
                sendIntent.putExtra("Code", stringBuilder.toString());
                stringBuilder.setLength(0);
                startActivity(sendIntent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Email not sent successfully, please contact the suppport team", Toast.LENGTH_SHORT).show();
            }
        }

        else if(v.getId()==R.id.email_layout_sign_in_btn) {

            // Launch Login activity

            Intent loginIntent = new Intent(Email.this,Login.class);
            startActivity(loginIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
