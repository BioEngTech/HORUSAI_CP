package vigi.patient.view.patient.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import vigi.patient.view.patient.home.HomePatientActivity;
import vigi.patient.view.patient.home.viewHolder.AppointmentsAdapter;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<Appointment> historyAppointmentsList;
    private EmptyRecyclerView.LayoutManager layoutManager;
    private EmptyRecyclerView recyclerView;
    private AppointmentService appointmentService;
    private CareProviderService careProviderService;
    private TreatmentService treatmentService;
    private ValueEventListener careProviderListener, treatmentListener, appointmentListener;
    private List<String> careProviderIds, treatmentsIds, appointmentsIds;
    private Comparator<Appointment> appointmentComparator;
    private List<CareProvider> careProvidersList;
    private List<Treatment> treatmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_appointments);

        historyAppointmentsList = new ArrayList<>();

        String currentPatientId = "1"; //TODO change with currentPatientTokenId
        setupUiComponents();
        setUpServices(currentPatientId);

        customizeActionBar();
        customizeToolBar();

    }

    private void setUpServices(String currentPatientId){

        appointmentListener = new HistoryActivity.AppointmentValueEventListener();
        appointmentService = new FirebaseAppointmentService();
        appointmentService.init();

        careProviderListener = new HistoryActivity.CareProviderValueEventListener();
        careProviderService = new FirebaseCareProviderService();
        careProviderService.init();

        treatmentListener = new HistoryActivity.TreatmentValueEventListener();
        treatmentService = new FirebaseTreatmentService();
        treatmentService.init();

        appointmentService.readAppointments(appointmentListener, currentPatientId);

    }

    private void customizeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Appointments record");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupUiComponents() {
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TextView title = findViewById(R.id.first_text);
        TextView subtitle = findViewById(R.id.second_text);
        title.setVisibility(View.GONE);
        subtitle.setVisibility(View.GONE);

    }

    public class AppointmentValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            historyAppointmentsList = new ArrayList<>();
            appointmentsIds = new ArrayList<>();

            for (DataSnapshot snapshotAppointment : dataSnapshot.getChildren()) {
                if (snapshotAppointment.child("status").getValue().equals("reviewed") || snapshotAppointment.child("status").getValue().equals("scanned")){
                    historyAppointmentsList.add(AppointmentConverter.getAppointmentFromDataSnapshot(snapshotAppointment));
                    appointmentsIds.add(snapshotAppointment.getKey());
                }
            }

            //order active appointments list to be displayed in HomePatientActivity
            appointmentComparator = Comparator.comparing(Appointment::getDate);
            Collections.sort(historyAppointmentsList, appointmentComparator);

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
            careProviderIds = historyAppointmentsList.stream().map(appointment -> appointment.getCareProviderId()).collect(toList());

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
            treatmentsIds = historyAppointmentsList.stream().map(appointment -> appointment.getTreatmentId()).collect(toList());

            for (DataSnapshot snapshotTreatmentInstance : dataSnapshot.getChildren()) {
                if(treatmentsIds.contains(snapshotTreatmentInstance.child("id").getValue())){
                    treatmentList.add(TreatmentConverter.getTreatmentFromDataSnapshot(snapshotTreatmentInstance));
                }
            }

            //display list of active appointments
            AppointmentsAdapter adapter = new AppointmentsAdapter(HistoryActivity.this, appointmentsIds, historyAppointmentsList, careProvidersList, treatmentList);
            recyclerView.setAdapter(adapter);
            recyclerView.setEmptyView(findViewById(R.id.empty_view)); // always put after the adapter was set

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
