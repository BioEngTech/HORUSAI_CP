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

    void readTreatments(ValueEventListener listener);

    void setAllTreatments(List<Treatment> treatments);

    List<Treatment> readTreatmentWithCategory(String category);

    Treatment readTreatment(String treatmentId);

    void addOnOperationCompleteListener(ValueEventListener valueEventListener);
}
