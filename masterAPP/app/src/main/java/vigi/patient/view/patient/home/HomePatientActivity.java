package vigi.patient.view.patient.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vigi.patient.R;


import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Appointment;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.appointment.api.AppointmentService;
import vigi.patient.presenter.service.appointment.impl.AppointmentConverter;
import vigi.patient.presenter.service.appointment.impl.FirebaseAppointmentService;
import vigi.patient.presenter.service.careProvider.api.CareProviderService;
import vigi.patient.presenter.service.careProvider.firebase.CareProviderConverter;
import vigi.patient.presenter.service.careProvider.firebase.FirebaseCareProviderService;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.FirebaseTreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.TreatmentConverter;
import vigi.patient.view.patient.cart.viewHolder.CartAdapter;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.home.viewHolder.AppointmentsAdapter;
import vigi.patient.view.patient.treatment.TreatmentSelectionActivity;
import vigi.patient.view.utils.drawable.CountDrawable;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static vigi.patient.view.utils.activity.ActivityUtils.jumpToActivity;

@SuppressWarnings("FieldCanBeLocal")
public class HomePatientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = getClass().getName();
    private Toolbar myToolbar;
    private FloatingActionButton bookingBtn;
    private ArrayList<Appointment> cartAppointmentsList, activeAppointmentsList;
    private ValueEventListener appointmentListener;
    private AppointmentService appointmentService;
    MenuInflater menuInflater;
    Menu menuToUpdate;
    private EmptyRecyclerView.LayoutManager layoutManager;
    private EmptyRecyclerView recyclerView;
    private CareProviderService careProviderService;
    private TreatmentService treatmentService;
    private ValueEventListener careProviderListener, treatmentListener;
    private List<String> careProviderIds, treatmentsIds, appointmentsIds;
    private List<CareProvider> careProvidersList;
    private List<Treatment> treatmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_appointments);

        cartAppointmentsList = new ArrayList<>();
        activeAppointmentsList = new ArrayList<>();

        String currentPatientId = "1"; //TODO change with currentPatientTokenId
        setUpServices(currentPatientId);

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
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

      }

    private void setUpServices(String currentPatientId){

        appointmentListener = new HomePatientActivity.AppointmentValueEventListener();
        appointmentService = new FirebaseAppointmentService();
        appointmentService.init(currentPatientId);

        careProviderListener = new HomePatientActivity.CareProviderValueEventListener();
        careProviderService = new FirebaseCareProviderService();
        careProviderService.init();

        treatmentListener = new HomePatientActivity.TreatmentValueEventListener();
        treatmentService = new FirebaseTreatmentService();
        treatmentService.init();

        appointmentService.readAppointments(appointmentListener);

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


    public class AppointmentValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            cartAppointmentsList = new ArrayList<>();
            activeAppointmentsList = new ArrayList<>();

            for (DataSnapshot snapshotAppointment : dataSnapshot.getChildren()) {
                if (snapshotAppointment.child("status").getValue().equals("cart")){
                    cartAppointmentsList.add(AppointmentConverter.getAppointmentFromDataSnapshot(snapshotAppointment));
                }
                else if (snapshotAppointment.child("status").getValue().equals("active")){
                    activeAppointmentsList.add(AppointmentConverter.getAppointmentFromDataSnapshot(snapshotAppointment));
                }
            }
            // update menu cart number
            setCount(HomePatientActivity.this, String.valueOf(cartAppointmentsList.size()), menuToUpdate); // In case there was one request in the cart
            careProviderService.readCareProviders(careProviderListener);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }


    public class CareProviderValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            careProvidersList = new ArrayList<>();
            careProviderIds = activeAppointmentsList.stream().map(appointment -> appointment.getCareProviderId()).collect(toList());

            for (DataSnapshot snapshotCareProvider : dataSnapshot.getChildren()) {
                if(careProviderIds.contains(snapshotCareProvider.child("id").getValue().toString())){
                    careProvidersList.add(CareProviderConverter.getCareProviderFromDataSnapshot(snapshotCareProvider));
                }
            }
            treatmentService.readTreatments(treatmentListener);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }


    public class TreatmentValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            treatmentList = new ArrayList<>();
            treatmentsIds = activeAppointmentsList.stream().map(appointment -> appointment.getTreatmentId()).collect(toList());

            for (DataSnapshot snapshotTreatmentInstance : dataSnapshot.getChildren()) {
                if(treatmentsIds.contains(snapshotTreatmentInstance.child("id").getValue())){
                    treatmentList.add(TreatmentConverter.getTreatmentFromDataSnapshot(snapshotTreatmentInstance));
                }
            }

            //display list of active appointments
            AppointmentsAdapter adapter = new AppointmentsAdapter(HomePatientActivity.this, appointmentsIds, activeAppointmentsList, careProvidersList, treatmentList);
            recyclerView.setAdapter(adapter);
            recyclerView.setEmptyView(findViewById(R.id.empty_view)); // always put after the adapter was set

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        setCount(HomePatientActivity.this, String.valueOf(cartAppointmentsList.size()), menu); // In case there was one request in the cart
        menuToUpdate = menu;

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

