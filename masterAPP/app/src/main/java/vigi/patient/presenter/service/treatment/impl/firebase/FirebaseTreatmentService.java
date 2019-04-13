package vigi.patient.presenter.service.treatment.impl.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;

public class FirebaseTreatmentService implements TreatmentService {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Treatment.class.getSimpleName());
    }

    @Override
    public void readTreatmentsWithCategory(ValueEventListener listener, String treatmentCategory) {
        addOnOperationCompleteListener(listener);
    }

    @Override
    public void addOnOperationCompleteListener(ValueEventListener valueEventListener) {
        databaseReference.addValueEventListener(valueEventListener);
    }

}
