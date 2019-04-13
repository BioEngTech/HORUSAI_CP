package vigi.patient.view.patient.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import vigi.patient.R;


import vigi.patient.model.services.Appointment;
import vigi.patient.view.patient.home.viewHolder.AppointmentsViewAdapter;
import vigi.patient.view.patient.treatment.SelectTreatmentActivity;

@SuppressWarnings("FieldCanBeLocal")
public class HomePatientActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = Activity.class.getName();

    //general layout vars
    private Toolbar toolbar;
    private FloatingActionButton bookingBtn;

    //recycler view vars
    private ArrayList<Appointment> appointmentsList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_home);

        // Set up views
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        bookingBtn = findViewById(R.id.appointment_btn);

        // Set up toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        // Set up booking btn
        bookingBtn.setOnClickListener(this);

        // get all user appointments
        getUserAppointments();

        // set as linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        adapter = new AppointmentsViewAdapter(appointmentsList);
        recyclerView.setAdapter(adapter);

        // TODO create dragging view to cancel an appointment

    }

    private void getUserAppointments(){
        appointmentsList = new ArrayList<>();
        Date date = new Date(20190413L);
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, Duration.ZERO, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, Duration.ZERO, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, Duration.ZERO, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, Duration.ZERO, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, Duration.ZERO, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
        appointmentsList.add(new Appointment(date, "report", 3, 20.0, 10.0, 5.0, Duration.ZERO, BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Appointment.AppointmentStatus.ACTIVE.categoryString()));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == bookingBtn.getId()){
            Intent chooseTreatmentIntent = new Intent(this, SelectTreatmentActivity.class);
            startActivity(chooseTreatmentIntent);
        }
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
}
