package vigi.patient.view.patient.treatment;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import vigi.patient.model.services.Treatment;
import vigi.patient.view.patient.treatment.viewHolder.CardsPagerTransformerShift;
import vigi.patient.view.patient.treatment.viewHolder.TreatmentsViewAdapter;
import vigi.patient.R;

@SuppressWarnings("FieldCanBeLocal")
public class SelectTreatmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static String TAG = Activity.class.getName();
    private ViewPager viewPager;
    private TreatmentsViewAdapter adapter;
    private List<Treatment> treatments;
    private Toolbar toolbar;
    private Spinner spinner;

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
        List<String> categories = new ArrayList<>();
        categories.add("All types of help");
        categories.add("Daily Assistance");
        categories.add("Medical Assistance");
        categories.add("Physical Therapy");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Give drop down style to the spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // TODO Several treatment objects should be in the database with id, title, image, category, description, duration, benefits...

        // Set treatments according to the default category selected in the spinner
        setTreatments();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        setTreatments();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void setTreatments(){

        // TODO Get category selected in the spinner and then go to the database and pick treatments related to that category

        // Example if the selected category was "All types of help"

        treatments = new ArrayList<>();
        treatments.add(new Treatment("12345","Company",getResources().getDrawable(R.drawable.image_company),"","","",""));
        treatments.add(new Treatment("12345","Hygiene and comfort",getResources().getDrawable(R.drawable.image_hygiene),"","","",""));
        treatments.add(new Treatment("12345","Kinesiotherapy",getResources().getDrawable(R.drawable.image_kinesiotherapy),"","","",""));
        treatments.add(new Treatment("12345","Daily tasks",getResources().getDrawable(R.drawable.image_daily),"","","",""));
        treatments.add(new Treatment("12345","Injection",getResources().getDrawable(R.drawable.image_injection),"","","",""));
        treatments.add(new Treatment("12345","Wound dressing",getResources().getDrawable(R.drawable.image_cut),"","","",""));
        treatments.add(new Treatment("12345","Advanced treatment",getResources().getDrawable(R.drawable.image_wound),"","","",""));
        treatments.add(new Treatment("12345","Follow-up",getResources().getDrawable(R.drawable.image_mental),"","","",""));
        treatments.add(new Treatment("12345","Screening",getResources().getDrawable(R.drawable.image_pressure),"","","",""));
        treatments.add(new Treatment("12345","Chest therapy",getResources().getDrawable(R.drawable.image_respiratory),"","","",""));
        treatments.add(new Treatment("12345","Massage therapy",getResources().getDrawable(R.drawable.image_massage),"","","",""));

        // Set up the view pager

        adapter = new TreatmentsViewAdapter(treatments,this);

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
