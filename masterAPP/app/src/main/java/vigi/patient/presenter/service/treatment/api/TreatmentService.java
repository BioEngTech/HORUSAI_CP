package vigi.patient.presenter.service.treatment.api;

import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.UUID;

import vigi.patient.model.services.Treatment;


/**
 * Typical service for CRUD operations
 */
public interface TreatmentService {

    void init();

    void readTreatmentsWithCategory(ValueEventListener listener, String treatmentCategory);

    void addOnOperationCompleteListener(ValueEventListener valueEventListener);
}
