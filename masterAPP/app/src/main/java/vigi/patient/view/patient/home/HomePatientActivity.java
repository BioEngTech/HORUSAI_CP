package vigi.patient.view.patient.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
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
    private FirebaseRecyclerAdapter<Appointment,AppointmentsViewAdapter.ViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mRefAppointment;
    private FirebaseDatabase mDatabase;


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
        //getUserAppointments();

        // set as linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // specify an adapter
        //TODO delete and replace by FirebasePatientService.getPatient().getkey(); Should this be done in readPatient?
        String ficticiousUserKey = "-LWgBDUjDLGpfzN8Xb-7";
        mDatabase = FirebaseDatabase.getInstance();
        mRefAppointment = mDatabase.getReference().child("Appointment");

        //TODO change the query to ease up onBindViewHolder job
        FirebaseRecyclerOptions<Appointment> options = new FirebaseRecyclerOptions.Builder<Appointment>()
                .setQuery(mRefAppointment, Appointment.class)
                .build();
        Log.d("1904 passou2", ficticiousUserKey);

        adapter = new AppointmentsViewAdapter(options, ficticiousUserKey);

        adapter.startListening();
        recyclerView.setAdapter(adapter);
        Log.d("1904 passou", "SIIIII");

        // TODO create dragging view to cancel an appointment

    }


    private void getUserAppointments(){
/*
        appointmentsList = new ArrayList<>();

        mRefPatient.child(ficticiousUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot appointment:dataSnapshot.getChildren()){

                    mRefAppointment.child(appointment.getKey())
                    appointmentsList.add(appointment.getKey().);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));
        appointmentsList.add(new Appointment("13h","Today","1h30","29$","Hygene and comfort","Anna Baker","4.4",getResources().getDrawable(R.drawable.image_photo),"","",getResources().getDrawable(R.drawable.image_photo)));

    */

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
