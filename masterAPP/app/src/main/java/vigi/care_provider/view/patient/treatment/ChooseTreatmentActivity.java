package vigi.care_provider.view.patient.treatment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import vigi.care_provider.view.patient.appointment.BookAppointmentsActivity;
import vigi.care_provider.R;

/**
 * TODO: Register the activity in the manifest afterwards
 */
public class ChooseTreatmentActivity extends AppCompatActivity implements View.OnClickListener{

    private Button featTwo;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_choose_treatment);

        featTwo = findViewById(R.id.featureTwo);

        next = findViewById(R.id.next);

        featTwo.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


        if (view.getId() == featTwo.getId()) {


            // TODO execute function and return treatment available --> getTreatments()


        } else if (view.getId() == next.getId()) {

            Intent launchUserIntent = new Intent(ChooseTreatmentActivity.this, BookAppointmentsActivity.class);
            startActivity(launchUserIntent);
            finish();
        }
    }
}