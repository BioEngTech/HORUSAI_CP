package vigi.care_provider.view.patient.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import vigi.care_provider.R;
import vigi.care_provider.view.patient.treatment.ChooseTreatmentActivity;

public class HomePatientActivity extends AppCompatActivity implements View.OnClickListener{

    private Button featOne;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        featOne = findViewById(R.id.featureOne);

        next = findViewById(R.id.next);

        featOne.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == featOne.getId()) {
            // TODO execute function and return Appointments --> getAppointments(patientId, day, hours)

        } else if (view.getId() == next.getId()) {

            Intent launchUserIntent = new Intent(HomePatientActivity.this, ChooseTreatmentActivity.class);
            startActivity(launchUserIntent);
            finish();
        }
    }
}
