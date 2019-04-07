package vigi.patient.view.patient.treatment;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;
import vigi.patient.R;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.FirebaseTreatmentService;
import vigi.patient.view.patient.appointment.BookAppointmentsActivity;


@SuppressWarnings("FieldCanBeLocal")
public class TreatmentDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = TreatmentDetailsActivity.class.getName();
    private Toolbar myToolbar;
    private ImageView imageTreatment;
    private TextView category;
    private TextView duration;
    private TextView description;
    private TextView benefits;
    private FloatingActionButton bookingBtn;
    private CollapsingToolbarLayout collapsingToolbar;
    private Treatment treatment;

    private TreatmentService treatmentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(R.layout.patient_treatment_details);

        // Get views present on the layout
        myToolbar = findViewById(R.id.toolbar);
        imageTreatment = findViewById(R.id.image);
        category = findViewById(R.id.category);
        duration = findViewById(R.id.duration);
        description = findViewById(R.id.description);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        benefits = findViewById(R.id.benefits);
        bookingBtn = findViewById(R.id.booking_btn);

        treatmentService = new FirebaseTreatmentService();
        treatmentService.init();

        // Fetch selected treatment form the database using id previously selected,
        // and display information according to the selected treatment, example below
        Intent intent = getIntent();
        Long treatmentId = Objects.requireNonNull(intent.getExtras()).getLong("treatmentId");
        String categoryName = Objects.requireNonNull(intent.getExtras()).getString("categoryName");

        Log.d(" treatmentId ", treatmentId.toString());
        readTreatment(categoryName, treatmentId);

        // Customize action bar / toolbar
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up booking btn
        bookingBtn.setOnClickListener(this);

    }


    private void readTreatment(String categoryName, Long treatmentId){

        // TODO set up views according to the information of the treatment selected
        treatment = treatmentService.readTreatment(treatmentId);

        Picasso.get().load(treatment.getImage().toString()).into(imageTreatment);
        collapsingToolbar.setTitle(treatment.getName());
        duration.setText(treatment.getExpectedTime().toString());
        category.setText(categoryName);
        description.setText(treatment.getDescription());
        benefits.setText(treatment.getBenefits());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == bookingBtn.getId()){
            Intent bookingIntent = new Intent(this, BookAppointmentsActivity.class);
            startActivity(bookingIntent);
        }
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

    // Action when back navigation button is pressed
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
    }
}
