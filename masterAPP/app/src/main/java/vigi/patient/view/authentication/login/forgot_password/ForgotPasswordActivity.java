package vigi.patient.view.authentication.login.forgot_password;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;

import vigi.patient.R;
import vigi.patient.presenter.connection.ConnectionEvaluator;
import vigi.patient.presenter.error.codes.FirebaseErrorCodes;
import vigi.patient.presenter.error.exceptions.AuthenticationException;
import vigi.patient.presenter.service.authentication.api.AuthenticationService;
import vigi.patient.presenter.service.authentication.impl.FirebaseAuthService;
import vigi.patient.view.vigi.activity.VigiGenerateNewPasswordActivity;

import static com.google.common.base.Preconditions.checkNotNull;
import static vigi.patient.view.utils.editText.EditTextUtils.getTrimmedText;

public class ForgotPasswordActivity extends AppCompatActivity implements VigiGenerateNewPasswordActivity {

    private static String TAG = ForgotPasswordActivity.class.getName();

    private ProgressBar fabProgressCircle;

    private FloatingActionButton continueBtn;
    private EditText emailText;
    private TextInputLayout emailInput;
    private TextView internetTextView;

    private boolean emailError = false;
    AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_forgot_password);

        setupUiComponents();

        authService = new FirebaseAuthService();
        authService.init();

    }

    @Override
    public void setupUiComponents() {
        // Make continue button respond to a click and disable it at beggining
        continueBtn = findViewById(R.id.continue_button);
        continueBtn.setEnabled(false);

        emailText = findViewById(R.id.email);

        // Implement TextWatcher for removing errors and enabling continue button
        emailText.addTextChangedListener(new ForgotPasswordTextWatcher(this));

        // Try to continue when enter is pressed in email text
        emailText.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_GO && continueBtn.isEnabled()) {
                continueBtn.performClick();
                return true;
            }
            return false;
        });

        emailText.setOnKeyListener((v, keyCode, keyEvent) -> {
            // Try to continue when enter is pressed in email text from the enter button
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    && continueBtn.isEnabled() ) {

                continueBtn.performClick();
                return true;
            }
            return false;
        });

        fabProgressCircle = findViewById(R.id.progress_circle);
        internetTextView = findViewById(R.id.internet);
        emailInput = findViewById(R.id.email_input_layout);

        customizeActionBar();
        customizeToolBar();
    }

    private void customizeActionBar() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setupClickListeners() {
        continueBtn.setOnClickListener(view -> {
            // Internet check
            ConnectionEvaluator checkNet = new ConnectionEvaluator();

            if (!checkNet.isConnectionAvailable(ForgotPasswordActivity.this)) {
                if (internetTextView.getVisibility() != View.INVISIBLE) return;
                else {
                    slideUp(internetTextView);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> slideDown(internetTextView), 3500);
                    return;
                }
            }

            // Check if emailText is still wrong
            if (hasEmailError()) {
                emailInput.requestFocus();
                return;
            }

            // Disable other views and start spinning loading
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            continueBtn.setImageResource(0);
            fabProgressCircle.setVisibility(View.VISIBLE);

            performGenerateNewPassword(authService, getTrimmedText(emailText));
        });
    }

    @Override
    public void performGenerateNewPassword(AuthenticationService authService, String user) {
        try {
            authService.generateNewPassword(user);
            authService.addGenerateNewPasswordCompleteListener(new GenerateNewPasswordCompleteListener());
        } catch (AuthenticationException e) {
            //TODO: Improvement needed!
            Toast.makeText(this, "Deu coco!", Toast.LENGTH_SHORT).show();
        }
    }

    // Stop spinning loader, enable movement
    private void loadingHandling(View spinView, View textView, FloatingActionButton btn) {
        spinView.setVisibility(View.GONE);
        btn.setImageResource(R.drawable.ic_continue_white_24dp);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        textView.requestFocus();
    }

    // Slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                  // toXDelta
                view.getHeight(),           // fromYDelta
                0);               // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // Slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                  // toXDelta
                0,               // fromYDelta
                view.getHeight());         // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    // Action when back arrow is pressed
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

    protected boolean hasEmailError() {
        return emailError;
    }

    protected void setEmailError(boolean emailError) {
        this.emailError = emailError;
    }

    public FloatingActionButton getContinueBtn() {
        return continueBtn;
    }

    public EditText getEmailText() {
        return emailText;
    }

    public TextInputLayout getEmailInput() {
        return emailInput;
    }

    private class GenerateNewPasswordCompleteListener implements OnCompleteListener<Void> {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                try {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    String errorText = FirebaseErrorCodes.exceptionType(errorCode);
                    setEmailError(true);
                    emailInput.setError(errorText);

                    loadingHandling(fabProgressCircle, emailText, continueBtn);
                } catch (ClassCastException e) {
                    //TODO: is this exception the right one?
                    // Internet problem
                    if (internetTextView.getVisibility() != View.INVISIBLE) {
                        internetTextView.setVisibility(View.VISIBLE);
                    } else {
                        slideUp(internetTextView);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> slideDown(internetTextView), 3500);
                    }
                    loadingHandling(fabProgressCircle, emailText, continueBtn);
                }
            }
        }
    }
}

