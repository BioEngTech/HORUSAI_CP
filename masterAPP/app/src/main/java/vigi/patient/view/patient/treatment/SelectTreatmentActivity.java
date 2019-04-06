package vigi.patient.view.patient.treatment;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.FirebaseTreatmentService;
import vigi.patient.view.patient.treatment.viewHolder.CardsPagerTransformerShift;
import vigi.patient.view.patient.treatment.viewHolder.TreatmentsViewAdapter;
import vigi.patient.R;

@SuppressWarnings("FieldCanBeLocal")
public class SelectTreatmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static String TAG = Activity.class.getName();
    private ViewPager viewPager;
    private TreatmentsViewAdapter adapter;
    private ArrayList<Treatment> treatments;
    private Toolbar toolbar;
    private Spinner spinner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefTreatment;
    private String category;
    private ArrayList<String> categories;
    private FirebaseTreatmentService TSer;
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_treatments);

        // Set up views
        toolbar = findViewById(R.id.toolbar);
        spinner = findViewById(R.id.type);

        // Customize action bar / toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner drop down elements

        mDatabase = FirebaseDatabase.getInstance();
        mRefTreatment = mDatabase.getReference().child("Treatment");

    }
    @Override
    protected void onStart() {
        super.onStart();
        CheckCategories();
    }

    private void CheckTreatments(String category) {
        new FirebaseTreatmentService().readTreatments( category, new TreatmentService() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onSuccess(DataSnapshot data) {

                //DO SOME THING WHEN GET DATA SUCCESS HERE
                treatments = new ArrayList<>();

                for (DataSnapshot treatment: data.getChildren()){
                    treatments.add(new Treatment(treatment.child("admittedjobs").getValue(),treatment.child("image").getValue(), treatment.child("expectedtime").getValue(), treatment.child("pricehint").getValue(), treatment.child("description").getValue(), treatment.child("name").getValue(), treatment.getKey(),  treatment.child("benefits").getValue()));
                }

                Log.d("1904 parent? ",category);
                adapter = new TreatmentsViewAdapter(category, treatments,getApplicationContext());

                viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(adapter);

                float density = getResources().getDisplayMetrics().density;
                int partialWidth = 45 * (int) density; // 45dp
                int pageMargin = 55 * (int) density; // 55dp

                int viewPagerPadding = partialWidth + pageMargin;

                viewPager.setPageMargin(pageMargin);
                viewPager.setPadding(pageMargin, 0, viewPagerPadding, 0);

                Point screen = new Point();
                getWindowManager().getDefaultDisplay().getSize(screen);
                float startOffset = (float)(viewPagerPadding)/(float)(screen.x - 2*viewPagerPadding);

                viewPager.setPageTransformer(false, new CardsPagerTransformerShift(1, 1,(float) 0.85, startOffset));

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

    private void CheckCategories() {
        new FirebaseTreatmentService().readCategories( new TreatmentService() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onSuccess(DataSnapshot data) {

                //DO SOME THING WHEN GET DATA SUCCESS HERE
                categories = new ArrayList<>();

                for (DataSnapshot treatment : data.getChildren()){
                    categories.add(treatment.getKey());
                }

                // Creating adapter for spinner
                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

                // Give drop down style to the spinner
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
                // TODO Several treatment objects should be in the database with id, title, image, category, description, duration, benefits...

                // Set treatments according to the default category selected in the spinner
                CheckTreatments(categories.get(0));

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = spinner.getSelectedItem().toString();
        CheckTreatments(category);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
