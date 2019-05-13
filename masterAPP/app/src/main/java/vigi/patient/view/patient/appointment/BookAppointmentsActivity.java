package vigi.patient.view.patient.appointment;

import android.app.Activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import vigi.patient.view.patient.appointment.viewHolder.CareProvidersViewAdapter;

@SuppressWarnings("FieldCanBeLocal")
public class BookAppointmentsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, AdapterView.OnItemSelectedListener {

    private static String TAG = Activity.class.getName();
    private Toolbar toolbar;
    private ArrayList<CareProvider> careProvidersList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_booking);

        // Set up views
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        spinner = findViewById(R.id.type);

        // Customize action bar / toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fetch care providers that can do the selected treatment for that day,
        // and display information according to that
        Intent intent = getIntent();
        String treatmentId = Objects.requireNonNull(intent.getExtras()).getString("treatmentId");

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH,0);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH,2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this,R.id.calendar)
                .range(startDate,endDate)
                .datesNumberOnScreen(7)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner drop down elements
        List<String> hours = new ArrayList<>();
        hours.add("10 AM");
        hours.add("11 AM");
        hours.add("12 AM");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,hours);

        // Give drop down style to the spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        // get all user appointments
        getUserAppointments();

        // set as linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        adapter = new CareProvidersViewAdapter(careProvidersList);
        recyclerView.setAdapter(adapter);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.vertical_margin);
        recyclerView.addItemDecoration(itemDecoration);

        // TODO create dragging view to cancel an appointment

    }

    private void getUserAppointments(){
        careProvidersList = new ArrayList<>();
        CareProvider careProviderExample = new CareProvider();
        careProviderExample.setName("John doe");
        careProvidersList.add(careProviderExample);
        CareProvider careProviderExample2 = new CareProvider();
        careProviderExample2.setName("John doe");
        careProvidersList.add(careProviderExample2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.toolbar_search || super.onOptionsItemSelected(item);
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

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}