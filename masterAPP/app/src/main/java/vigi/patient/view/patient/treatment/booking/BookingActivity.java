package vigi.patient.view.patient.treatment.booking;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import vigi.patient.R;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.patient.treatment.booking.viewHolder.BookingViewAdapter;
import vigi.patient.view.utils.drawable.CountDrawable;
import vigi.patient.view.utils.recyclerView.ItemOffsetDecoration;
import vigi.patient.view.vigi.activity.VigiActivity;
import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("FieldCanBeLocal")
public class BookingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, AdapterView.OnItemSelectedListener, VigiActivity {

    private String TAG = getClass().getName();
    private final static String CHOSEN_TREATMENT = "chosenTreatment";
    private Toolbar toolbar;
    private ArrayList<CareProvider> careProvidersList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Spinner spinnerOrder;
    private String treatmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch care providers that can do the selected treatment for that day,
        // and display information according to that
        Intent intent = getIntent();
        treatmentName = Objects.requireNonNull(intent.getExtras()).getString(CHOSEN_TREATMENT);

        setContentView(R.layout.treatment_booking);
        setupUiComponents();

        getUserAppointments();
        setUpRecyclerView();

    }

    private void setUpSpinner(){
        spinnerOrder.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Rating");
        categories.add("Price");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.adapter_treatment_booking_spinner,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapter);
    }

    private void setUpCalendar(){
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH,2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this,R.id.calendar)
                .range(startDate,endDate)
                .datesNumberOnScreen(7)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                getUserAppointments();
            }
        });
    }

    private void setUpRecyclerView(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookingViewAdapter(this,careProvidersList);
        recyclerView.setAdapter(adapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.recycler_view_vertical_margin);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void setupUiComponents() {
        recyclerView = findViewById(R.id.recycler_view);
        spinnerOrder = findViewById(R.id.order);
        customizeActionBar();
        customizeToolBar();
        setUpCalendar();
        setUpSpinner();
    }

    @Override
    public void setupClickListeners() {

    }

    private void customizeActionBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setTitle(treatmentName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getUserAppointments(){
        careProvidersList = new ArrayList<>();
        CareProvider careProviderExample = new CareProvider();
        careProviderExample.setName("John doe");
        careProvidersList.add(careProviderExample);

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


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {


    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}