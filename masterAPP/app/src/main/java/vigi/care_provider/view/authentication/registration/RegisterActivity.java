package vigi.care_provider.view.authentication.registration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.util.Log;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import vigi.care_provider.model.entities.Patient;
import vigi.care_provider.presenter.error.codes.FirebaseErrorCodes;
import vigi.care_provider.presenter.service.authentication.api.AuthenticationService;
import vigi.care_provider.presenter.service.authentication.impl.FirebaseAuthService;
import vigi.care_provider.view.patient.treatment.TreatmentsListActivity;
import vigi.care_provider.R;

import vigi.care_provider.view.utils.dialog.VigiDatePickerDialog;
import vigi.care_provider.view.utils.dialog.VigiErrorDialog;
import vigi.care_provider.view.utils.dialog.VigiPictureAlertDialog;
import vigi.care_provider.view.utils.editText.EditTextUtils;
import vigi.care_provider.view.utils.span.VigiClickableSpan;
import vigi.care_provider.view.vigi.activity.VigiActivity;
import vigi.care_provider.view.vigi.activity.VigiRegisterActivity;

import static com.google.common.base.Preconditions.checkNotNull;
import static vigi.care_provider.view.utils.activity.ActivityUtils.CAMERA;
import static vigi.care_provider.view.utils.activity.ActivityUtils.GALLERY;
import static vigi.care_provider.view.utils.activity.ActivityUtils.hideKeyboardFrom;
import static vigi.care_provider.view.utils.activity.ActivityUtils.jumpToActivity;
import static vigi.care_provider.view.utils.activity.ActivityUtils.jumpToPhoneCameraActivity;
import static vigi.care_provider.view.utils.editText.EditTextUtils.getTrimmedText;

public class RegisterActivity extends AppCompatActivity implements VigiRegisterActivity {

    private static String TAG = RegisterActivity.class.getName();

    private EditText passwordText;
    private EditText emailText;
    private String errorText;
    private Button registerBtn;
    private ImageView spin;
    private TextView termsAndConditions;
    private EditText birthdayText;
    private EditText nameText;
    private EditText phoneText;
    private String errorCode = "";
    private LinearLayout background;
    private AppCompatImageView photoImageView;
    private String newPatientkey;
    private Uri contentURI;
    CountryCodePicker ccp;

    private AuthenticationService authService;

    StorageReference storageRef, imagesRef;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRefPatient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initiation_register);

        setupUiComponents();
        setupClickListeners();

        setupAuthService(authService);

        //Initialise FirebaseErrorCodes instances
        storageRef = FirebaseStorage.getInstance().getReference();
        imagesRef = storageRef.child("images/profiles");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRefPatient = mFirebaseDatabase.getReference("Patient");

        // TODO when user selects a country flag for his phoneText number, automatically change the hint on the right side according to the format of the number in that country
        String code = ccp.getSelectedCountryCode();
    }

    @Override
    public void setupUiComponents() {
        emailText = findViewById(R.id.initiationRegister_email);
        passwordText = findViewById(R.id.initiationRegister_Password);
        nameText = findViewById(R.id.initiationRegister_name);
        phoneText = findViewById(R.id.initiationRegister_phoneNumber);
        registerBtn = findViewById(R.id.initiationRegister_registerButton);
        spin = findViewById(R.id.initiationRegister_spinner);
        termsAndConditions = findViewById(R.id.initiationRegister_termsAndConditions);
        birthdayText = findViewById(R.id.initiationRegister_birthday);
        photoImageView = findViewById(R.id.initiationRegister_ProfilePhoto);
        background = findViewById(R.id.initiationRegister_background);
        ccp = findViewById(R.id.initiationRegister_ccp);

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
            String date = new StringBuilder()
                    .append(dayOfMonth)
                    .append("/")
                    .append(month)
                    .append("/")
                    .append(year).toString();

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
        registerBtn.setOnClickListener(v -> {
            // Disable movement and start spinning loader
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            spinVisibility(spin, View.VISIBLE, registerBtn, RegisterActivity.this);

            ccp.registerCarrierNumberEditText(phoneText);

            if (ccp.isValidFullNumber()){
                performRegister(authService, getTrimmedText(emailText), getTrimmedText(passwordText));
            } else {
                errorHandling(spin, background, registerBtn, RegisterActivity.this, "Please enter a valid phoneText number.");
            }
        });

        //TODO translations in strings.xml
        photoImageView.setOnClickListener(v -> new VigiPictureAlertDialog(this)
                .showDialog("Select photoImageView from gallery", "Capture photoImageView from camera"));
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
        ScrollView scrollView = findViewById(R.id.initiationRegister_scrollview);
        scrollView.setVerticalScrollBarEnabled(false);
    }

    private void customizeActionBar() {
        Toolbar myToolbar = findViewById(R.id.initiationRegister_toolbar);
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
                photoImageView.setBackgroundResource(0); // to delete the drawable that was inside the circle
                photoImageView.setImageURI(contentURI);
            } else if (requestCode == CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                //get uri
                final FileOutputStream fos;
                try {
                    fos = openFileOutput("my_new_image.jpg", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), thumbnail, "Title", null);

                    Log.d("NAMASTE", "path is " + path);

                    //TODO photoImageView from camera is not the same size as border
                    contentURI = Uri.parse(path);
                    photoImageView.setBackgroundResource(0); // to delete the drawable that was inside the circle
                    photoImageView.setImageURI(contentURI);

                    Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    Toast.makeText(RegisterActivity.this, "Image wasn't saved!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    // Spinning handling
    private static void spinVisibility(View spinView, int visibility, Button btn, Context context) {
        if (visibility == View.INVISIBLE) {
            btn.setEnabled(true); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
            spinView.clearAnimation();
            spinView.setVisibility(visibility);
            btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else if (visibility == View.VISIBLE) {
            btn.setEnabled(false); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
            btn.setTextColor(context.getResources().getColor(R.color.transparent));
            spinView.setVisibility(visibility);
            spinView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely));
        }
    }

    // Stop spinning loader, enable movement and launch error dialog
    private void errorHandling(View spinView, View backgroundView, Button btn, Activity activity, String text) {
        backgroundView.performClick();
        spinVisibility(spinView, View.INVISIBLE, btn, activity);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        VigiErrorDialog alert = new VigiErrorDialog(this);
        alert.showDialog(text);
    }

    // Action when back arrow is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
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
    public void performRegister(AuthenticationService authService, String email, String password) {
        try {
            authService.register(email, password);
            authService.addRegisterCompleteListener(this, new RegisterCompleteListener());
        } catch (IllegalArgumentException e) {
            errorHandling(spin, background, registerBtn, RegisterActivity.this, "Please enter a valid sign up, all fields are required.");
        }
    }

    private class RegisterCompleteListener implements OnCompleteListener<AuthResult> {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                //create random key for new Patient
                newPatientkey = mRefPatient.push().getKey();

                //save image in firestore
                final StorageReference fileRef = imagesRef.child(newPatientkey + ".jpg");
                fileRef.putFile(contentURI).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        //pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();

                        //save new Patient data to FirebaseErrorCodes
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Got the download URL for 'users/me/profile.png'
                            Log.d("NAMASTE uri", uri.toString());
                            Uri downloadUri = uri;
                            String profilephotopath = downloadUri.toString();

                            //define Patient instance
                            Patient patient = new Patient();
                            patient.setName(nameText.getText().toString());
                            patient.setImage(profilephotopath); //get path to firestore
                            patient.setEmail(emailText.getText().toString());
                            String full_phonenumber= ccp.getSelectedCountryCode() +  phoneText.getText().toString();
                            Log.d("NAMASTE phonenumber",full_phonenumber);
                            patient.setPhoneNumber(full_phonenumber);

                            patient.setId(FirebaseAuth.getInstance().getCurrentUser().toString());

                            Log.d("NAMASTE newPatientkey", newPatientkey);

                            mRefPatient.child(newPatientkey).setValue(patient);

                            jumpToActivity(RegisterActivity.this, TreatmentsListActivity.class, true,
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        }).addOnFailureListener(exception -> {
                            // Handle any errors
                            Toast.makeText(RegisterActivity.this, "Couldn't upload image successfully", Toast.LENGTH_SHORT).show();
                        });

                    } else {
                        String message = task1.getException().toString();
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });

            } else { // if task not successful
                try {
                    errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    FirebaseErrorCodes exceptionThrowed = new FirebaseErrorCodes();

                    errorText = exceptionThrowed.exceptionType(errorCode);

                    errorHandling(spin, background, registerBtn, RegisterActivity.this, errorText);
                } catch (ClassCastException e) {

                    errorHandling(spin, background, registerBtn, RegisterActivity.this, "Internet connection is not available.");
                }
            }
        }
    }
}