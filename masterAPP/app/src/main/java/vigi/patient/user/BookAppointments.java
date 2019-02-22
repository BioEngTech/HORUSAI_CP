package vigi.patient.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vigi.patient.R;

public class BookAppointments extends AppCompatActivity implements View.OnClickListener{

    private Button featThree;
    private Button featFour;
    private EditText featFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_book_appointments);

        featThree = findViewById(R.id.featureThree);
        featFour = findViewById(R.id.featureFour);
        featFive = findViewById(R.id.featureFive);

        featThree.setOnClickListener(this);
        featFour.setOnClickListener(this);

        // TODO execute function and return care providers searched --> searchCareProviders(arg)
        // TODO (HINT) need to call the function each time the edittext is changed

    }

    @Override
    public void onClick(View view) {


        if (view.getId() == featThree.getId()) {


            // TODO execute function and return care providers --> getCareProviders(patientLocation,treatment)


        }
        else if (view.getId() == featFour.getId()) {

            // TODO execute function and return care providers list ordered by the arg --> sortingCareProviders(arg)

        }

    }
}