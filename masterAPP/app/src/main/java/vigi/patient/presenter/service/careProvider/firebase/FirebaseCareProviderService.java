package vigi.patient.presenter.service.careProvider.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vigi.patient.model.entities.CareProvider;
import vigi.patient.presenter.service.careProvider.api.CareProviderService;

import static java.util.stream.Collectors.toList;

public class FirebaseCareProviderService implements CareProviderService {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    List<CareProvider> allCareProviders;
    private Query databaseQueryService, databaseQueryReview;

    @Override
    public void init() {
        allCareProviders = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(CareProvider.class.getSimpleName());

    }

    @Override
    public void readCareProviders(ValueEventListener listener) {
        addOnOperationCompleteListener(listener);
    }

    @Override
    public List<CareProvider> readCareProviderWithTreatment(String job) {

        return allCareProviders.stream()
                .filter(careProvider -> job.equals(careProvider.getJob()))
                .collect(toList());
    }


    @Override
    public CareProvider readCareProvider(UUID careProviderId) {

        return allCareProviders.stream()
                .filter(careProvider -> careProviderId.equals(careProvider.getId()))
                .findFirst()
                .orElse(new CareProvider());
    }

    @Override
    public void readCareProvidersTreatments(String careProviderId, ValueEventListener valueEventListener) {

        Log.d("NAMASTE id", careProviderId);
        databaseQueryService = firebaseDatabase.getReference("Services").orderByChild(careProviderId).equalTo(true);
        databaseQueryService.addValueEventListener(valueEventListener);

    }


    @Override
    public void addOnOperationCompleteListener(ValueEventListener valueEventListener) {
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void setAllCareProviders(List<CareProvider> allCareProviders) {
        this.allCareProviders = allCareProviders;
    }

    public List<CareProvider> getAllCareProviders() {
        return allCareProviders;
    }


}
