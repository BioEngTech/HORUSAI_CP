package vigi.patient.view.authentication.registration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.hbb20.CountryCodePicker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import vigi.patient.model.entities.Patient;
import vigi.patient.presenter.error.codes.FirebaseErrorCodes;
import vigi.patient.presenter.service.authentication.api.AuthenticationService;
import vigi.patient.presenter.service.authentication.impl.FirebaseAuthService;
import vigi.patient.presenter.service.patient.api.PatientService;
import vigi.patient.presenter.service.patient.impl.FirebasePatientService;
import vigi.patient.R;
import vigi.patient.view.authentication.login.LoginActivity;
import vigi.patient.view.patient.home.HomePatientActivity;
import vigi.patient.view.utils.dialog.VigiDatePickerDialog;
import vigi.patient.view.utils.dialog.VigiErrorDialog;
import vigi.patient.view.utils.span.VigiClickableSpan;
import vigi.patient.view.utils.dialog.VigiPictureAlertDialog;
import vigi.patient.view.vigi.activity.VigiRegisterActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.common.base.Preconditions.checkNotNull;
import static vigi.patient.view.utils.activity.ActivityUtils.CAMERA;
import static vigi.patient.view.utils.activity.ActivityUtils.GALLERY;
import static vigi.patient.view.utils.activity.ActivityUtils.hideKeyboardFrom;
import static vigi.patient.view.utils.activity.ActivityUtils.jumpToActivity;
import static vigi.patient.view.utils.activity.ActivityUtils.jumpToPhoneCameraActivity;
import static vigi.patient.view.utils.editText.EditTextUtils.getTrimmedText;

public class RegisterActivity extends AppCompatActivity implements VigiRegisterActivity {

    private static String TAG = RegisterActivity.class.getName();

    private EditText passwordText;
    private EditText emailText;
    private Button registerBtn;
    private TextView signInText;
    private ImageView spin;
    private CircleImageView photo;
    private TextView termsAndConditions;
    private EditText birthdayText;
    private EditText nameText;
    private EditText phoneText;
    private LinearLayout background;
    private RelativeLayout photoBorder;
    private Uri contentURI;
    CountryCodePicker ccp;

    private AuthenticationService authService;
    private PatientService patientService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_register);

        setupUiComponents();
        setupClickListeners();
        setupAuthService();
        setupPatientService();
    }

    @Override
    public void setupUiComponents() {
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        nameText = findViewById(R.id.name);
        phoneText = findViewById(R.id.phone_number);
        registerBtn = findViewById(R.id.register_button);
        signInText = findViewById(R.id.sign_in_button);
        spin = findViewById(R.id.spinner);
        termsAndConditions = findViewById(R.id.terms_and_conditions);
        birthdayText = findViewById(R.id.birthday);
        photoBorder = findViewById(R.id.profile_photo_view);
        photo = findViewById(R.id.profile_photo);
        background = findViewById(R.id.background);
        ccp = findViewById(R.id.ccp);

        // Make layout respond to focus
        nameText.setOnFocusChangeListener((nameTextView, b) -> hideKeyboardFrom(nameTextView, phoneText, emailText, passwordText));
        phoneText.setOnFocusChangeListener((phoneTextView, b) -> hideKeyboardFrom(phoneTextView, nameText, emailText, passwordText));
        emailText.setOnFocusChangeListener((emailTextView, b) -> hideKeyboardFrom(emailTextView, nameText, phoneText, passwordText));

        // Setup input type of password programmatically to avoid getting bold
        passwordText.setTransformationMethod(new PasswordTransformationMethod());
        passwordText.setOnFocusChangeListener((passwordTextView, b) -> hideKeyboardFrom(passwordTextView, nameText, phoneText, emailText));
        passwordText.setOnEditorActionListener((v, actionId, event) -> {
            // Try to RegisterActivity when enter is pressed in password text
            if (actionId == EditorInfo.IME_ACTION_GO) {
                registerBtn.performClick();
                return true;
            }
            return false;
        });
        passwordText.setOnKeyListener((v, keyCode, event) -> {
            // Try to RegisterActivity when enter is pressed in password text from the enter button on
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (nameText.hasFocus()) {
                    birthdayText.performClick();
                } else if (passwordText.hasFocus()) {
                    registerBtn.performClick();
                }
                return true;
            }
            return false;
        });

        // Activate Birthday Dialog
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            month++;
            String date = String.valueOf(dayOfMonth) + "/" + month + "/" + year;
            birthdayText.setText(date);
            emailText.requestFocus();
        };

        birthdayText.setKeyListener(null);
        birthdayText.setOnFocusChangeListener((v, hasFocus) -> {

            if (!birthdayText.hasFocus()) {
                return;
            }

            new VigiDatePickerDialog(this, dateSetListener).showDialog();

            emailText.requestFocus();
        });

        setupTermsAndConditions();
        removeScrollableIndicator();
        customizeActionBar();
        customizeToolBar();
    }

    @Override
    public void setupClickListeners() {
        signInText.setOnClickListener(v -> jumpToActivity(RegisterActivity.this, LoginActivity.class, true));
        registerBtn.setOnClickListener(v -> {

            // Register the number
            ccp.registerCarrierNumberEditText(phoneText);

            // Disable movement and start spinning loader
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            spinVisibility(this, spin, View.VISIBLE, registerBtn);


            //  Check if photo was selected, check if phone is valid, and check if birthday and name are filled
            if (photo.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_camera_white_24dp).getConstantState())) {
                background.performClick();
                stopSpinningLoader(spin, registerBtn);
                new VigiErrorDialog(RegisterActivity.this).showDialog("Please select a photo.");
            } else if (!ccp.isValidFullNumber()){
                background.performClick();
                stopSpinningLoader(spin, registerBtn);
                new VigiErrorDialog(RegisterActivity.this).showDialog("Please enter a valid phone number.");
            } else if (birthdayText.getText().length()==0){
                background.performClick();
                stopSpinningLoader(spin, registerBtn);
                new VigiErrorDialog(RegisterActivity.this).showDialog("Please enter a valid sign up, all fields are required.");
            } else {
                performRegister(authService, getTrimmedText(emailText), getTrimmedText(passwordText));
            }
        });

        //TODO translations in strings.xml
        photoBorder.setOnClickListener(v -> new VigiPictureAlertDialog(this)
                .showDialog("Select photo from gallery", "Capture photo from camera"));
    }

    //Should this be generic to reuse?
    private void setupTermsAndConditions() {
        VigiClickableSpan clickTermsConditions = new VigiClickableSpan(this);
        SpannableString ss = new SpannableString(getString(R.string.terms_and_conditions_text));
        ss.setSpan(clickTermsConditions, 0, getString(R.string.terms_and_conditions_text).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsAndConditions.setText(ss);
        termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void removeScrollableIndicator() {
        ScrollView scrollView = findViewById(R.id.scrollview);
        scrollView.setVerticalScrollBarEnabled(false);
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

    private void setupAuthService() {
        authService = new FirebaseAuthService();
        authService.init();
    }

    private void setupPatientService() {
        patientService = new FirebasePatientService();
        patientService.init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && userGrantedCameraAccess(grantResults)) {
            jumpToPhoneCameraActivity(this);
        }
    }

    private boolean userGrantedCameraAccess(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == GALLERY && data != null) {
                contentURI = data.getData();
                //TODO save image to firestore
                Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                photo.setBackgroundResource(0); // to delete the drawable that was inside the circle
                photo.setImageURI(contentURI);
            } else if (requestCode == CAMERA) {
                checkNotNull(data.getExtras());
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                checkNotNull(thumbnail);

                //get uri
                final FileOutputStream fos;
                try {

                    fos = openFileOutput("my_new_image.jpg", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), thumbnail, "Title", null);

                    //TODO photo from camera is not the same size as border
                    contentURI = Uri.parse(path);
                    photo.setBackgroundResource(0); // to delete the drawable that was inside the circle
                    photo.setImageURI(contentURI);

                    Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    Toast.makeText(RegisterActivity.this, "Image wasn't saved!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
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

    private void stopSpinningLoader(View spinView, Button btn) {
        spinVisibility(this, spinView, View.INVISIBLE, btn);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // Action when back arrow is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
                return true;
        }
        return false;
    }

    // Action when back navigation button is pressed
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
    }

    @Override
    public void performRegister(AuthenticationService authService, String email, String password) {
        try {
            authService.register(email, password);
            authService.addRegisterCompleteListener(new RegisterCompleteListener());
        } catch (IllegalArgumentException e) {
            background.performClick();
            stopSpinningLoader(spin, registerBtn);
            new VigiErrorDialog(RegisterActivity.this).showDialog("Please enter a valid sign up, all fields are required.");        }
    }

    private class RegisterCompleteListener implements OnCompleteListener<AuthResult> {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {

                Patient patient = new Patient();
                patient.setName(nameText.getText().toString());
                patient.setImage(contentURI.toString());/*temporary value, is changed before actually saving object */
                patient.setEmail(emailText.getText().toString());
                patient.setPhoneNumber(ccp.getSelectedCountryCode() +  phoneText.getText().toString());
                patient.setId(authService.getCurrentUserString());
                // TODO create and save birthday aswell

                patientService.createPatient(patient);

                jumpToActivity(RegisterActivity.this, HomePatientActivity.class, true,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            }  else {
                String errorText = "";
                try {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    errorText = FirebaseErrorCodes.exceptionType(errorCode);
                } catch (ClassCastException e){
                    errorText = "Internet connection is not available.";
                } finally {
                    background.performClick();
                    stopSpinningLoader(spin, registerBtn);
                    new VigiErrorDialog(RegisterActivity.this).showDialog(errorText);
                }
            }
        }
    }
}