package vigi.patient.view.authentication.registration;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.model.entities.Patient;
import vigi.patient.presenter.error.codes.FirebaseErrorCodes;
import vigi.patient.view.authentication.login.LoginActivity;
import vigi.patient.view.utils.dialog.ErrorDialog;
import vigi.patient.view.patient.home.HomePatientActivity;
import vigi.patient.R;


import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("FieldCanBeLocal")
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener, TextView.OnKeyListener {

    private static String TAG = Activity.class.getName();

    private EditText passwordText;
    private EditText emailText;
    private FirebaseAuth authFire;
    private String errorText;
    private String code;
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
    private RelativeLayout photoBorder;
    private CircleImageView photo;
    private CountryCodePicker ccp;
    private static final int CAMERA = 1888;
    private static final int GALLERY = 1889;
    private StorageReference storageRef, imagesRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRefPatient;
    private String newPatientkey;
    private String profilePhotopath;
    private Uri contentURI;
    private TextView signInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout
        setContentView(R.layout.authentication_register);

        // Views
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone_number);
        registerBtn = findViewById(R.id.register_button);
        spin = findViewById(R.id.spinner);
        termsAndConditions = findViewById(R.id.terms_and_conditions);
        scrollView = findViewById(R.id.scrollview);
        birthday = findViewById(R.id.birthday);
        myToolbar = findViewById(R.id.toolbar);
        photoBorder = findViewById(R.id.profile_photo_view);
        photo = findViewById(R.id.profile_photo);
        background = findViewById(R.id.background);
        ccp = findViewById(R.id.ccp);
        signInBtn = findViewById(R.id.sign_in_button);

        //Initialise firebase instances
        storageRef = FirebaseStorage.getInstance().getReference();
        imagesRef = storageRef.child("images/profiles");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRefPatient = mFirebaseDatabase.getReference("Patient");

        //remove scrollable indicator
        scrollView.setVerticalScrollBarEnabled(false);

        // Customize action bar / toolbar
        setSupportActionBar(myToolbar);

        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Make views respond to a click
        photoBorder.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);

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

        // Activate birthday Dialog
        birthday.setKeyListener(null);

        birthday.setOnFocusChangeListener((v, hasFocus) -> {

            if (!birthday.hasFocus()) {
                return;
            }

            Calendar cal = Calendar.getInstance();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);

            long hundred_ten_years = 1000 * 60 * 60 * 24 * 39600L;

            dialog.getDatePicker().setMinDate(cal.getTimeInMillis() - hundred_ten_years);
            dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();

            emailText.requestFocus();

        });

        dateSetListener = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = dayOfMonth + "/" + month + "/" + year;
            birthday.setText(date);
            emailText.requestFocus();
        };

        // Set an underline to the terms and conditions
        String terms_conditions_text = "Terms and Conditions";

        final SpannableString ss = new SpannableString(terms_conditions_text);


        ClickableSpan click_terms_conditions = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(RegisterActivity.this, "Terms and Conditions are still missing.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(RegisterActivity.this.getResources().getColor(R.color.colorWhite));
            }
        };

        ss.setSpan(click_terms_conditions, 0, terms_conditions_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndConditions.setText(ss);
        termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());


        // TODO when user selects a country flag for his phone number, automatically change the hint on the right side according to the format of the number in that country
        code = ccp.getSelectedCountryCode();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        // Try to RegisterActivity when enter is pressed in password text
        if (actionId == EditorInfo.IME_ACTION_GO) {

            registerBtn.performClick();

            return true;
        }
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // Try to RegisterActivity when enter is pressed in password text from the enter button on
        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            switch (event.getAction()) {

                case KeyEvent.ACTION_DOWN:
                    if (name.hasFocus()) {
                        birthday.performClick();
                    } else if (passwordText.hasFocus()) {
                        registerBtn.performClick();
                    }
                    break;

                case KeyEvent.ACTION_UP:
                    break;
            }
            return true;
        }
        return false;
    }

    // Implement click funcionalities
    @Override
    public void onClick(View v) {

        if(v.getId()== signInBtn.getId()) { // Try to sign in
            Intent signInIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(signInIntent);
            finish();

        } else if (v.getId() == registerBtn.getId()) { // Try to Register

            // Disable movement and start spinning loader
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            spinVisibility(spin, View.VISIBLE, registerBtn, RegisterActivity.this);

            ccp.registerCarrierNumberEditText(phone); // TODO que isto?

            //  Check if photo was selected, check if phone is valid, and check if birthday and name are filled
            if (photo.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_camera_white_24dp).getConstantState())) {
                errorHandling(spin, background, registerBtn, RegisterActivity.this, "Please select a photo.");
            } else if (!ccp.isValidFullNumber()){
                errorHandling(spin, background, registerBtn, RegisterActivity.this, "Please enter a valid phone number.");
            } else if (birthday.getText().length()==0 || name.getText().length() == 0){
                errorHandling(spin, background, registerBtn, RegisterActivity.this, "Please enter a valid sign up, all fields are required.");
            } else{
                registerAttempt();
            }

        } else if (v.getId() == photoBorder.getId()) { // Try to set up photo
            // Ask user to select a photo or to take one
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems, (dialog, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;
                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (RegisterActivity.this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                ///method to get Images
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(RegisterActivity.this, "Your Permission is needed to get access the camera!", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);

            }
        } else {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();

                //TODO save image to firestore
                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                photo.setBackgroundResource(0); // to delete the drawable that was inside the circle
                photo.setImageURI(contentURI);
            }
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            //get uri
            final FileOutputStream fos;
            try {
                fos = openFileOutput("my_new_image.jpg", Context.MODE_PRIVATE);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), thumbnail, "Title", null);

                Log.d(TAG, "Path is " + path);

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

    private void registerAttempt() {
        try {
            // Initialize FirebaseErrorCodes auth
            authFire = FirebaseAuth.getInstance();

            // Get EditText Strings
            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            authFire.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        //create random key for new Patient
                        newPatientkey = mRefPatient.push().getKey();

                        //save image in firestore
                        final StorageReference fileRef = imagesRef.child(newPatientkey + ".jpg");
                        fileRef.putFile(contentURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    //pd.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();

                                    //save new Patient data to FirebaseErrorCodes
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Got the download URL for 'users/me/profile.png'
                                            Log.d(TAG, "Uri: " + uri.toString());
                                            String profilephotopath = uri.toString();

                                            //define Patient instance
                                            Patient patient = new Patient();
                                            // TODO also add birthday to database
                                            patient.setName(name.getText().toString());
                                            patient.setImage(profilephotopath); //get path to firestore
                                            patient.setEmail(emailText.getText().toString());
                                            String full_phonenumber= ccp.getSelectedCountryCode() +  phone.getText().toString();
                                            patient.setPhoneNumber(full_phonenumber);

                                            patient.setId(FirebaseAuth.getInstance().getCurrentUser().toString());

                                            Log.d(TAG,"Patient key: " + newPatientkey);

                                            mRefPatient.child(newPatientkey).setValue(patient);

                                            Intent launchUserIntent = new Intent(RegisterActivity.this, HomePatientActivity.class);
                                            launchUserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(launchUserIntent);
                                            finish();

                                        }
                                    }).addOnFailureListener(exception -> {
                                        // Handle any errors
                                        Toast.makeText(RegisterActivity.this, "Couldn't upload image successfully", Toast.LENGTH_SHORT).show();
                                    });

                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
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
            });
        } catch (IllegalArgumentException e) {
            errorHandling(spin, background, registerBtn, RegisterActivity.this, "Please enter a valid sign up, all fields are required.");
        }
    }

    // Focus handle
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && !name.hasFocus() && !phone.hasFocus() && !passwordText.hasFocus() && !emailText.hasFocus()) {
            InputMethodManager input = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
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
        } else if (visibility == View.VISIBLE) {
            btn.setEnabled(false); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
            btn.setTextColor(context.getResources().getColor(R.color.transparent));
            spinView.setVisibility(visibility);
            spinView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely));
        }
    }

    // Stop spinning loader, enable movement and launch error dialog
    private static void errorHandling(View spinView, View backgroundView, Button btn, Activity activity, String text) {
        backgroundView.performClick();
        spinVisibility(spinView, View.INVISIBLE, btn, activity);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ErrorDialog alert = new ErrorDialog();
        alert.showDialog(activity, text);
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

}