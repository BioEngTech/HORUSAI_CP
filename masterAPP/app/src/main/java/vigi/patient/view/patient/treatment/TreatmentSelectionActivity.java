package vigi.patient.view.patient.treatment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import vigi.patient.R;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.FirebaseTreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.TreatmentConverter;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.patient.treatment.viewHolder.TreatmentsViewAdapter;
import vigi.patient.view.utils.drawable.CountDrawable;
import vigi.patient.view.vigi.activity.VigiActivity;
import vigi.patient.view.vigi.web.VigiHtml;
import static com.google.common.base.Preconditions.checkNotNull;

public class TreatmentSelectionActivity extends AppCompatActivity implements VigiActivity {

    private String TAG = getClass().getName();
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private Toolbar toolbar;
    private TextView dailyAssistance;
    private TextView medicalAssistance;
    private TreatmentsViewAdapter adapter;
    private String category;
    private List<String> categories;
    private ValueEventListener treatmentListener;
    private TreatmentService treatmentService;
    private List<Treatment> treatmentsWithCategory;
    private int treatmentsSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treatment_selection);
        setupUiComponents();
        setupTreatments(); // TODO while treatments are being retrieved from database a spinner should run
        setupClickListeners();
    }

    @Override
    public void setupUiComponents() {
        categories = Treatment.TreatmentCategory.getCategories();
        viewPager = findViewById(R.id.view_pager);
        dotLayout = findViewById(R.id.dot_layout);
        dailyAssistance = findViewById(R.id.daily_category);
        medicalAssistance = findViewById(R.id.medical_category);

        customizeActionBar();
        customizeToolBar();
    }


    private void customizeActionBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setTitle("Select treatment");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupTreatments(){
        TreatmentsChangeListener viewListener = new TreatmentsChangeListener();
        viewPager.addOnPageChangeListener(viewListener);

        treatmentListener = new TreatmentSelectionActivity.VigiValueEventListener();
        treatmentService = new FirebaseTreatmentService();
        treatmentService.init();
    }

    @Override
    public void setupClickListeners() {
        dailyAssistance.setOnClickListener(view -> {
            dailyAssistance.setBackgroundResource(R.drawable.utils_rectangle_rounded_200dp_full_blue_medium);
            medicalAssistance.setBackgroundResource(R.drawable.utils_rectangle_rounded_200dp_border_0point5dp_white);
            category = Treatment.TreatmentCategory.DAILY_ASSISTANCE.toString();
            treatmentService.readTreatments(treatmentListener);
        });

        medicalAssistance.setOnClickListener(view -> {
            medicalAssistance.setBackgroundResource(R.drawable.utils_rectangle_rounded_200dp_full_blue_medium);
            dailyAssistance.setBackgroundResource(R.drawable.utils_rectangle_rounded_200dp_border_0point5dp_white);
            category = Treatment.TreatmentCategory.MEDICAL_ASSISTANCE.toString();
            treatmentService.readTreatments(treatmentListener);
        });
        dailyAssistance.performClick(); // DEFAULT CATEGORY
    }

    private void addDotsIndicator(int position, int treatmentsSize){
        dotLayout.removeAllViews();
        IntStream.range(0, treatmentsSize).forEach(i -> {
            TextView dot = new TextView(TreatmentSelectionActivity.this);
            dot.setText(VigiHtml.fromHtml("&#8226;"));
            dot.setTextSize(24);
            dot.setTextColor(ContextCompat.getColor(TreatmentSelectionActivity.this,
                    i == position ? R.color.colorBlueMedium : R.color.colorBlueSoft));
            dotLayout.addView(dot);
        });
    }


    private class TreatmentsChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            TreatmentsViewAdapter.currentPosition = position;
            addDotsIndicator(position, treatmentsSize);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    }

    private void notifyDataChanged(List<Treatment> treatments) {

        treatmentService.setAllTreatments(treatments);
        treatmentsWithCategory = treatmentService.readTreatmentWithCategory(category);

        adapter = new TreatmentsViewAdapter(treatmentsWithCategory, this);

        viewPager.setAdapter(adapter);
        treatmentsSize = treatmentsWithCategory.size();

        TreatmentsViewAdapter.currentPosition = 0;
        addDotsIndicator(0, treatmentsSize);
    }

    // Action when back navigation button is pressed
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
    }

    public void setCount(Context context, String count, Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        CountDrawable badge;

        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        }else{
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        // TODO get amount of requests in the cart and update cart icon with that value
        setCount(this, "1", menu); // In case there was one request in the cart
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            case R.id.search:
                Intent careProviderSearchIntent = new Intent(this, SearchActivity.class);
                startActivity(careProviderSearchIntent);
                return true;
            case R.id.cart:
                Intent checkCartIntent = new Intent(this, CartActivity.class);
                startActivity(checkCartIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class VigiValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Treatment> treatmentList = new ArrayList<>();

            for (DataSnapshot snapshotTreatment : dataSnapshot.getChildren()) {
                treatmentList.add(TreatmentConverter.getTreatmentFromDataSnapshot(snapshotTreatment));
            }
            notifyDataChanged(treatmentList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            notifyDataChanged(new ArrayList<>());
        }
    }

}
