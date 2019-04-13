package vigi.patient.view.patient.treatment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.FirebaseTreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.TreatmentConverter;
import vigi.patient.view.patient.treatment.viewHolder.CardsPagerTransformerShift;
import vigi.patient.view.patient.treatment.viewHolder.TreatmentsViewAdapter;
import vigi.patient.R;

import static vigi.patient.model.services.Treatment.TreatmentCategory.DAILY_ASSISTANCE;

public class SelectTreatmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static String TAG = SelectTreatmentActivity.class.getName();

    private ViewPager viewPager;
    private TreatmentsViewAdapter adapter;
    private Toolbar toolbar;
    private Spinner spinner;
    private String category;
    //TODO: We must know beforehand the categories that exist
    private List<String> categories;
    ArrayAdapter<String> dataAdapter;

    ValueEventListener treatmentListener;
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

        categories = Treatment.TreatmentCategory.getCategories();
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

        // Give drop down style to the spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // TODO Several treatment objects should be in the database with id, title, image, categoryString, description, duration, benefits...

        float density = getResources().getDisplayMetrics().density;
        int partialWidth = 45 * (int) density; // 45dp
        int pageMargin = 55 * (int) density; // 55dp
        int viewPagerPadding = partialWidth + pageMargin;
        Point screen = new Point();
        getWindowManager().getDefaultDisplay().getSize(screen);
        float startOffset = (float)(viewPagerPadding)/(float)(screen.x - 2 * viewPagerPadding);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setPageMargin(pageMargin);
        viewPager.setPadding(pageMargin, 0, viewPagerPadding, 0);
        viewPager.setPageTransformer(false, new CardsPagerTransformerShift(1, 1,(float) 0.85, startOffset));

        //NOTE: DAILY_ASSISTANCE is the default one!
        category = Treatment.TreatmentCategory.DAILY_ASSISTANCE.categoryString();
        treatmentListener = new VigiValueEventListener();
        treatmentService = new FirebaseTreatmentService();
        treatmentService.init();
        treatmentService.readTreatmentsWithCategory(treatmentListener, DAILY_ASSISTANCE.categoryString());

    }

    public void notifyDataChanged(List<Treatment> treatments) {
        adapter = new TreatmentsViewAdapter(category, treatments, getApplicationContext());
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = spinner.getSelectedItem().toString();
        treatmentService.readTreatmentsWithCategory(treatmentListener, category);
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

    public class VigiValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Treatment> treatmentList = new ArrayList<>();
            for (DataSnapshot snapshotTreatment : dataSnapshot.getChildren()) {
                treatmentList.add(TreatmentConverter.getTreatmentFromDataSnapshot(snapshotTreatment));
                notifyDataChanged(treatmentList);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            notifyDataChanged(new ArrayList<>());
        }
    }
}
