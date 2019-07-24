package vigi.patient.view.patient.careProvider.booking;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import vigi.patient.R;
import vigi.patient.view.patient.careProvider.booking.viewHolder.BookingAdapter;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.utils.drawable.CountDrawable;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;
import vigi.patient.view.utils.recyclerView.ItemOffsetDecoration;
import vigi.patient.view.vigi.activity.VigiActivity;
import static com.google.common.base.Preconditions.checkNotNull;

public class BookingActivity extends AppCompatActivity implements VigiActivity {

    private String TAG = getClass().getName();
    ArrayList<String> daysOfWeek = new ArrayList<>();
    HashMap<String, ArrayList<String>> availableHours = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cprovider_booking);
        setupUiComponents();
    }

    @Override
    public void setupUiComponents() {
        customizeActionBar();
        customizeToolBar();
        setUpSpinner();
        getAvailableHoursForTheNextWeeks(); // TODO get for two months
        setUpRecyclerView();
    }

    @Override
    public void setupClickListeners() {

    }

    private void getAvailableHoursForTheNextWeeks(){
        daysOfWeek = new ArrayList<>();
        availableHours = new HashMap<>();

        daysOfWeek.add("Tue");
        ArrayList<String> hours = new ArrayList<>();
        hours.add("10:00");
        hours.add("10:30");
        hours.add("11:00");
        hours.add("11:30");
        availableHours.put("Tue",hours);

        daysOfWeek.add("Wed");
        ArrayList<String> hours1 = new ArrayList<>();
        hours1.add("10:00");
        hours1.add("10:30");
        hours1.add("11:00");
        hours1.add("11:30");
        hours1.add("10:00");
        hours1.add("10:30");
        hours1.add("11:00");
        hours1.add("11:30");
        hours1.add("10:00");
        hours1.add("10:30");
        hours1.add("11:00");
        hours1.add("11:30");
        availableHours.put("Wed",hours1);

        daysOfWeek.add("Thu");
        ArrayList<String> hours2 = new ArrayList<>();
        hours2.add("10:00");
        hours2.add("10:30");
        hours2.add("11:00");
        hours2.add("11:30");
        availableHours.put("Thu",hours2);

        daysOfWeek.add("Fri");
        availableHours.put("Fri",hours1);

        daysOfWeek.add("Sat");
        availableHours.put("Sat",hours1);


    }

    private void setUpSpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Hygene and comfort"); // TODO get treatments that the care provider does
        categories.add("Company");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.adapter_cprovider_booking_spinner,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerOrder = findViewById(R.id.patient_name);
        spinnerOrder.setAdapter(adapter);
    }

    private void customizeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setTitle("Care Provider Name"); // TODO set here care provider name
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {

        EmptyRecyclerView recyclerView = findViewById(R.id.recycler_view);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.recycler_view_vertical_margin);
        recyclerView.addItemDecoration(itemDecoration);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        BookingAdapter adapter = new BookingAdapter(this,daysOfWeek,availableHours);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
    }

    public void setCount(Context context, String count, Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        CountDrawable badge;

        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
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

}
