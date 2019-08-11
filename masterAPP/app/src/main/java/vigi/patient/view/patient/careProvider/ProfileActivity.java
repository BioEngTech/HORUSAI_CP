package vigi.patient.view.patient.careProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import vigi.patient.R;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.entities.Patient;
import vigi.patient.model.services.Appointment;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.appointment.api.AppointmentService;
import vigi.patient.presenter.service.appointment.impl.AppointmentConverter;
import vigi.patient.presenter.service.appointment.impl.FirebaseAppointmentService;
import vigi.patient.presenter.service.careProvider.api.CareProviderService;
import vigi.patient.presenter.service.careProvider.firebase.FirebaseCareProviderService;
import vigi.patient.presenter.service.patient.api.PatientService;
import vigi.patient.presenter.service.patient.impl.FirebasePatientService;
import vigi.patient.presenter.service.patient.impl.PatientConverter;
import vigi.patient.presenter.service.treatment.api.TreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.FirebaseTreatmentService;
import vigi.patient.presenter.service.treatment.impl.firebase.TreatmentConverter;
import vigi.patient.view.patient.careProvider.booking.BookingActivity;
import vigi.patient.view.patient.careProvider.viewHolder.ReviewsAdapter;
import vigi.patient.view.patient.careProvider.viewHolder.ServicesAdapter;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.utils.drawable.CountDrawable;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;
import vigi.patient.view.utils.recyclerView.ItemOffsetDecoration;
import vigi.patient.view.vigi.activity.VigiActivity;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static vigi.patient.view.utils.activity.ActivityUtils.jumpToActivity;

@SuppressWarnings("FieldCanBeLocal")
public class ProfileActivity extends AppCompatActivity implements VigiActivity {

    private String TAG = getClass().getName();
    private ArrayList<Treatment> treatments;
    private ArrayList<Appointment> appointments;
    private CareProvider careProvider;
    private final static String CHOSEN_CAREPROVIDER = "chosenCareProvider";
    private TextView rating, field, name, treatmentsNumber, reviewsNumber;
    private ValueEventListener servicesListener, appointmentsListener, treatmentsListener, patientsListener;
    private CareProviderService careProviderService, reviewsService;
    private TreatmentService treatmentsService;
    private AppointmentService appointmentService;
    private PatientService patientService;
    private List<String> treatmentsIds, patientsIds;
    private EmptyRecyclerView recyclerViewServices, recyclerViewReviews;
    private ArrayList<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cprovider_profile);

        Intent intent = getIntent();
        careProvider = (CareProvider) Objects.requireNonNull(intent.getExtras()).getSerializable(CHOSEN_CAREPROVIDER);
        treatments = new ArrayList<>();
        appointments = new ArrayList<>();

        setupUiComponents();
        setUpServicesRecyclerView();
        setUpReviewsRecyclerView();
        setupClickListeners();

    }

    @Override
    public void setupUiComponents() {

        rating = findViewById(R.id.rating);
        rating.setText(String.valueOf(careProvider.getRating()));
        field = findViewById(R.id.field);
        field.setText(careProvider.getJob());
        name = findViewById(R.id.name);
        name.setText(careProvider.getName());

        treatmentsNumber = findViewById(R.id.treatmentsNumber);
        reviewsNumber = findViewById(R.id.reviewsNumber);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.smoothScrollTo(0,0);

        customizeActionBar();
        customizeToolBar();
    }

    public void setupClickListeners() {
        TextView booking = findViewById(R.id.booking);
        booking.setOnClickListener(v -> jumpToActivity(this, BookingActivity.class, false));
    }

    private void setUpServicesRecyclerView() {
        recyclerViewServices = findViewById(R.id.recycler_view_services);
        recyclerViewServices.setNestedScrollingEnabled(false);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewServices.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.recycler_view_vertical_margin);
        recyclerViewServices.addItemDecoration(itemDecoration);

        servicesListener = new ProfileActivity.ServicesValueEventListener();
        careProviderService = new FirebaseCareProviderService();
        careProviderService.init();

        treatmentsListener = new ProfileActivity.TreatmentValueEventListener();
        treatmentsService = new FirebaseTreatmentService();
        treatmentsService.init();

        careProviderService.readCareProvidersTreatments(careProvider.getId(), servicesListener);
    }

    private void setUpReviewsRecyclerView() {
        recyclerViewReviews = findViewById(R.id.recycler_view_reviews);
        recyclerViewReviews.setNestedScrollingEnabled(false);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewReviews.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.recycler_view_vertical_margin);
        recyclerViewReviews.addItemDecoration(itemDecoration);

        patientsListener = new ProfileActivity.PatientValueEventListener();
        patientService = new FirebasePatientService();
        patientService.init();

        appointmentsListener = new ProfileActivity.AppointmentValueEventListener();
        appointmentService = new FirebaseAppointmentService();
        appointmentService.init();

        appointmentService.readCareProviderAppointments(appointmentsListener, careProvider.getId());
    }

    public class ServicesValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            treatmentsIds = new ArrayList<>();

            for (DataSnapshot snapshotService : dataSnapshot.getChildren()) {
                treatmentsIds.add(snapshotService.getKey());
            }

            treatmentsNumber.setText(String.valueOf(treatmentsIds.size()));
            treatmentsService.readTreatments(treatmentsListener);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }


    public class TreatmentValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            treatments = new ArrayList<>();

            for (DataSnapshot snapshotTreatment : dataSnapshot.getChildren()) {
                if (treatmentsIds.contains(snapshotTreatment.getKey())) {
                    treatments.add(TreatmentConverter.getTreatmentFromDataSnapshot(snapshotTreatment));
                }
            }

            ServicesAdapter adapter = new ServicesAdapter(treatments);
            recyclerViewServices.setAdapter(adapter);
            recyclerViewServices.setEmptyView(findViewById(R.id.empty_view_services));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }

    public class PatientValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            patients = new ArrayList<>();
            patientsIds = appointments.stream().map(appointment -> appointment.getPatientId()).collect(toList());

            for (DataSnapshot snapshotPatient : dataSnapshot.getChildren()) {
                if (patientsIds.contains(snapshotPatient.child("tokenid").getValue())) {
                    patients.add(PatientConverter.getPatientFromDataSnapshot(snapshotPatient));
                }
            }

            ReviewsAdapter adapter = new ReviewsAdapter(appointments, patients, careProvider);
            recyclerViewReviews.setAdapter(adapter);
            recyclerViewReviews.setEmptyView(findViewById(R.id.empty_view_reviews)); // always put after the adapter was set

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    public class AppointmentValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            appointments = new ArrayList<>();
            patients = new ArrayList<>();

            for (DataSnapshot snapshotAppointment : dataSnapshot.getChildren()) {
                if (snapshotAppointment.child("status").getValue().equals("reviewed")) {
                    appointments.add(AppointmentConverter.getAppointmentFromDataSnapshot(snapshotAppointment));
                }
            }
            reviewsNumber.setText(String.valueOf(appointments.size()));

            patientService.readPatients(patientsListener);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    private void customizeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setTitle(careProvider.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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