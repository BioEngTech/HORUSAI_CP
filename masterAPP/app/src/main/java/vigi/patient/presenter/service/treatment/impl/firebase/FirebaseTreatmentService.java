package vigi.patient.presenter.service.treatment.impl.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;

public class FirebaseTreatmentService implements TreatmentService {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    List<Treatment> allTreatments;

    @Override
    public void init() {
        allTreatments = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Treatment.class.getSimpleName());

    }

    @Override
    public void readTreatments(ValueEventListener listener) {
        addOnOperationCompleteListener(listener);
    }

    @Override
    public List<Treatment> readTreatmentWithCategory(String category) {

        return allTreatments.stream()
                .filter(treatment -> category.equals(treatment.getCategory().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public Treatment readTreatment(UUID treatmentId) {
        return allTreatments.stream()
                .filter(treatment -> treatmentId.equals(treatment.getId()))
                .findFirst()
                .orElse(new Treatment());
    }

    @Override
    public void addOnOperationCompleteListener(ValueEventListener valueEventListener) {
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void setAllTreatments(List<Treatment> allTreatments) {
        this.allTreatments = allTreatments;
    }

    public List<Treatment> getAllTreatments() {
        return allTreatments;
    }

}
