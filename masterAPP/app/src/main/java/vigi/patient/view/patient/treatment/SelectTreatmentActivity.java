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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.FirebaseTreatmentService;
import vigi.patient.view.patient.treatment.viewHolder.CardsPagerTransformerShift;
import vigi.patient.view.patient.treatment.viewHolder.TreatmentsViewAdapter;
import vigi.patient.R;

@SuppressWarnings("FieldCanBeLocal")
public class SelectTreatmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static String TAG = SelectTreatmentActivity.class.getName();

    private ViewPager viewPager;
    private TreatmentsViewAdapter adapter;
    private Toolbar toolbar;
    private Spinner spinner;
    private String category;
    //TODO: We must know beforehand the categories that exist
    private List<String> categories = new ArrayList(){{add("category1");add("category2");}};
    ArrayAdapter<String> dataAdapter;

    TreatmentService treatmentService;

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




        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

        // Give drop down style to the spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // TODO Several treatment objects should be in the database with id, title, image, categoryString, description, duration, benefits...





        treatmentService = new FirebaseTreatmentService();
        treatmentService.init();
        List<Treatment> treatments = treatmentService.readTreatments();

        adapter = new TreatmentsViewAdapter(category, treatments, getApplicationContext());

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = spinner.getSelectedItem().toString();

        List<Treatment> categoryTreatments = treatmentService.readTreatmentsWithCategory(category);

        //TODO: show categoryTreatments?
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

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
