package vigi.patient.view.patient.treatment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import vigi.patient.R;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.FirebaseTreatmentService;
import vigi.patient.view.patient.appointment.BookAppointmentsActivity;
import vigi.patient.view.patient.treatment.viewHolder.CardsPagerTransformerShift;
import vigi.patient.view.patient.treatment.viewHolder.TreatmentsViewAdapter;


@SuppressWarnings("FieldCanBeLocal")
public class TreatmentDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = Activity.class.getName();
    private Toolbar myToolbar;
    private ImageView imageTreatment;
    private TextView category;
    private TextView duration;
    private TextView description;
    private TextView benefits;
    private FloatingActionButton bookingBtn;
    private CollapsingToolbarLayout collapsingToolbar;
    private Treatment treatment;

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

        // Fetch selected treatment form the database using id previously selected,
        // and display information according to the selected treatment, example below
        Intent intent = getIntent();
        String treatmentId = Objects.requireNonNull(intent.getExtras()).getString("treatmentId");
        String category_name = Objects.requireNonNull(intent.getExtras()).getString("category_name");

        Log.d(" treatmentId ", treatmentId);
        setTreatmentDetails(category_name, treatmentId);

        // Customize action bar / toolbar
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up booking btn
        bookingBtn.setOnClickListener(this);

    }


    private void setTreatmentDetails(String category_name, String id){

        // TODO set up views according to the information of the treatment selected

        new FirebaseTreatmentService().readTreatment( category_name, id, new TreatmentService() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onSuccess(DataSnapshot data) {

                //DO SOME THING WHEN GET DATA SUCCESS HERE
                treatment = new Treatment(data.child("admittedjobs").getValue(),data.child("image").getValue(), data.child("expectedtime").getValue(), data.child("pricehint").getValue(), data.child("description").getValue(), data.child("name").getValue(), data.getKey(), data.child("benefits").getValue() );

                Picasso.get().load(treatment.getImage()).into(imageTreatment);
                collapsingToolbar.setTitle(treatment.getName());
                duration.setText(treatment.getExpectedtime());
                category.setText(category_name);
                description.setText(treatment.getDescription());
                benefits.setText(treatment.getBenefits());

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });


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
