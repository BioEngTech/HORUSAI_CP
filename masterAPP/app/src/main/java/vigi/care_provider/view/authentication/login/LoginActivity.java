package vigi.care_provider.view.authentication.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

import vigi.care_provider.presenter.service.authentication.api.AuthenticationService;
import vigi.care_provider.presenter.service.authentication.impl.FirebaseAuthService;
import vigi.care_provider.view.authentication.login.forgot_password.ForgotPasswordActivity;
import vigi.care_provider.view.authentication.registration.RegisterActivity;
import vigi.care_provider.view.patient.home.HomePatientActivity;
import vigi.care_provider.R;
import vigi.care_provider.view.utils.dialog.VigiErrorDialog;
import vigi.care_provider.presenter.error.codes.FirebaseErrorCodes;
import vigi.care_provider.view.vigi.activity.VigiLoginActivity;

import static com.google.common.base.Preconditions.checkNotNull;
import static vigi.care_provider.view.utils.activity.ActivityUtils.jumpToActivity;
import static vigi.care_provider.view.utils.activity.ActivityUtils.hideKeyboardFrom;
import static vigi.care_provider.view.utils.editText.EditTextUtils.getTrimmedText;

public class LoginActivity extends AppCompatActivity implements VigiLoginActivity {

    private static String TAG = LoginActivity.class.getName();

    private EditText passwordText;
    private EditText emailText;
    private Button loginBtn;
    private TextView signUpBtn;
    private TextView lostPassBtn;
    private ImageView spin;
    private LinearLayout background;

    private AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initiation_login);

        setupUiComponents();

        setupAuthService(authService);
    }

    @Override
    public void setupUiComponents() {
        emailText = findViewById(R.id.initiationLogin_email);
        emailText.setOnFocusChangeListener((emailView, b) -> hideKeyboardFrom(emailView, passwordText));

        setupPasswordTextComponent();

        loginBtn = findViewById(R.id.initiationLogin_loginButton);
        signUpBtn = findViewById(R.id.initiationLogin_signUpButton);
        lostPassBtn = findViewById(R.id.initiationLogin_forgotPassButton);
        spin = findViewById(R.id.initiationLogin_spinner);
        background = findViewById(R.id.initiationLogin_background);

        removeScrollableIndicator();
        customizeActionBar();
        customizeToolBar();
    }

    private void setupPasswordTextComponent() {
        passwordText = findViewById(R.id.initiationLogin_password);
        passwordText.setOnFocusChangeListener((passwordView, b) -> hideKeyboardFrom(passwordView, emailText));
        // Setup input type of password programmatically to avoid getting bold
        passwordText.setTransformationMethod(new PasswordTransformationMethod());

        // Make password respond to enter
        passwordText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            // Try to Log In when enter is pressed in password text
            if (actionId == EditorInfo.IME_ACTION_GO) {
                checkNotNull(loginBtn);
                loginBtn.performClick();
                return true;
            }
            return false;
        });

        // Try to Log In when enter is pressed in password text from the enter button
        passwordText.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                checkNotNull(loginBtn);
                loginBtn.performClick();
                return true;
            }
            return false;
        });
    }

    private void removeScrollableIndicator() {
        ScrollView scrollView = findViewById(R.id.initiationLogin_scrollView);
        scrollView.setVerticalScrollBarEnabled(false);
    }

    private void customizeActionBar() {
        android.support.v7.widget.Toolbar myToolbar = findViewById(R.id.initiationLogin_toolbar);
        setSupportActionBar(myToolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupAuthService(AuthenticationService authService) {
        authService = new FirebaseAuthService();
        authService.init();
    }

    @Override
    public void setupClickListeners() {
        signUpBtn.setOnClickListener(v -> {
            jumpToActivity(this, RegisterActivity.class, true);
        });

        loginBtn.setOnClickListener(v -> {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            spinVisibility(this, spin, View.VISIBLE, loginBtn);

            performLogin(authService, getTrimmedText(emailText), getTrimmedText(passwordText));
        });

        lostPassBtn.setOnClickListener(v -> jumpToActivity(this, ForgotPasswordActivity.class, false));
    }

    // Spinning handling
    private static void spinVisibility(Context context, View spinView, int visibility, Button btn) {
        if (visibility == View.INVISIBLE) {
            btn.setEnabled(true);
            spinView.clearAnimation();
            spinView.setVisibility(visibility);
            btn.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else if(visibility == View.VISIBLE) {
            btn.setEnabled(false);
            btn.setTextColor(ContextCompat.getColor(context, R.color.transparent));
            spinView.setVisibility(visibility);
            spinView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely));
        }
    }

    private void stopSpinningLoader(ImageView spinView, Button btn) {
        spinVisibility(this, spinView, View.INVISIBLE, btn);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    @Override
    public void performLogin(AuthenticationService authService, String email, String password) {
        authService.login(email, password);
        authService.addLoginCompleteListener(this, new LoginCompleteListener());
    }

    private class LoginCompleteListener implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task task) {
            if (task.isSuccessful()) {
                jumpToActivity(LoginActivity.this, HomePatientActivity.class, true,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                String errorText = "";
                try {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    errorText = FirebaseErrorCodes.exceptionType(errorCode);
                } catch (ClassCastException e){
                    //TODO: is this exception the right one?
                    errorText = "Internet connection is not available.";
                } finally {
                    background.performClick();
                    stopSpinningLoader(spin, loginBtn);
                    new VigiErrorDialog(LoginActivity.this).showDialog(errorText);
                }
            }
        }
    }
}

