package vigi.patient.view.patient.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;

import vigi.patient.R;


import vigi.patient.model.services.Appointment;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.home.viewHolder.AppointmentsAdapter;
import vigi.patient.view.patient.treatment.TreatmentSelectionActivity;
import vigi.patient.view.utils.drawable.CountDrawable;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;
import static vigi.patient.view.utils.activity.ActivityUtils.jumpToActivity;

@SuppressWarnings("FieldCanBeLocal")
public class HomePatientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = getClass().getName();
    private Toolbar myToolbar;
    private FloatingActionButton bookingBtn;
    private ArrayList<Appointment> appointmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_appointments);

        getUserAppointments();
        customizeActionBar();
        customizeToolBar();
        setUpRecyclerView();
        setUpNavigationDrawer();
        setupClickListeners();

    }

    public void setupClickListeners() {
        bookingBtn = findViewById(R.id.appointment_btn);
        bookingBtn.setOnClickListener(v -> jumpToActivity(this, TreatmentSelectionActivity.class, false));
    }

    private void setUpNavigationDrawer(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpRecyclerView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.recycler_view);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AppointmentsAdapter adapter = new AppointmentsAdapter(appointmentsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(findViewById(R.id.empty_view)); // always put after the adapter was set
    }

    private void customizeActionBar() {
        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Homecare");
    }

    private void getUserAppointments() {
        appointmentsList = new ArrayList<>();
        Date date = new Date(20190413L);
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, 10, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, 10, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, 10, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, 10, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, 10, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, 10, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

