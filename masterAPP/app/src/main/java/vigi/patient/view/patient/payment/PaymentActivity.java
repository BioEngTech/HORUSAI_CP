package vigi.patient.view.patient.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import vigi.patient.R;
import vigi.patient.model.entities.Patient;
import vigi.patient.presenter.service.patient.api.PatientService;
import vigi.patient.presenter.service.patient.impl.FirebasePatientService;
import vigi.patient.presenter.service.patient.impl.PatientConverter;
import vigi.patient.view.patient.address.AddressActivity;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.vigi.activity.VigiActivity;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class PaymentActivity extends AppCompatActivity implements VigiActivity {

    Patient patient;
    private ValueEventListener patientListener;
    private PatientService patientService;
    private EditText paymentText, saveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_payment);

        String currentPatientId = "1"; //TODO change with currentPatientTokenId
        setupUiComponents();
        setUpServices(currentPatientId);
        setupClickListeners();

        customizeActionBar();
        customizeToolBar();

    }

    private void customizeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setupUiComponents() {
        paymentText = findViewById(R.id.payment);
        saveText = findViewById(R.id.save);
    }

    @Override
    public void setupClickListeners() {

        paymentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        saveText.setOnClickListener(view -> {
            //TODO save new address

        });

    }


    private void setUpServices(String currentPatientId) {

        patientListener = new PaymentActivity.PatientValueEventListener();
        patientService = new FirebasePatientService();
        patientService.init();
        patientService.readPatient(patientListener, currentPatientId);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class PatientValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            patient = new Patient();
            for (DataSnapshot snapshotPatient : dataSnapshot.getChildren()) {
                patient = PatientConverter.getPatientFromDataSnapshot(snapshotPatient);

            }
            paymentText.setText(patient.getPayment());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
