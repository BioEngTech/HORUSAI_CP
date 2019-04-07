package vigi.patient.presenter.service.treatment.impl.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.treatment.api.TreatmentService;


public class FirebaseTreatmentService implements TreatmentService {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    DatabaseReference currentReference;

    private volatile boolean allTreatmentsLoaded = false;

    @Override
    public void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Treatment.class.getSimpleName());
    }

    @Override
    public boolean createTreatment(Treatment treatment) {

        try {
            currentReference = databaseReference.push();
            databaseReference.child(currentReference.getKey()).setValue(treatment);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public Treatment readTreatment(Long treatmentId) {
        List<Treatment> treatmentList = readTreatments();

        for (Treatment treatment : treatmentList) {
            if (treatmentId.toString().equals(treatment.getId().toString())) {
                return treatment;
            }
        }

        return new Treatment();
    }

    @Override
    public List<Treatment> readTreatments() {

        List<Treatment> treatmentList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshotTreatment : dataSnapshot.getChildren()) {
                    treatmentList.add(TreatmentConverter.getTreatmentFromDataSnapshot(snapshotTreatment));
                }

                allTreatmentsLoaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: Handle case where something goes wrong and no treatments are fetched!!
                allTreatmentsLoaded = true;
            }
        });

        waitForLoadingTreatments();

        return treatmentList;
    }

    private void waitForLoadingTreatments() {
        try {
            while (!allTreatmentsLoaded) {
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        allTreatmentsLoaded = true;
    }

    @Override
    public List<Treatment> readTreatmentsWithCategory(String treatmentCategory) {
        List<Treatment> treatmentList = readTreatments();

        return treatmentList.stream()
                .filter(treatment -> treatmentCategory.equals(treatment.getCategory().categoryString()))
                .collect(Collectors.toList());
    }

    @Override
    public Treatment updateTreatment(Treatment treatment) {
        return null;
    }

    @Override
    public boolean deleteTreatment(Long TreatmentId) {
        return false;
    }
}
