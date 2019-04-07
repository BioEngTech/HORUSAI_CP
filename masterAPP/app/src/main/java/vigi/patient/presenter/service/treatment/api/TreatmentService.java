package vigi.patient.presenter.service.treatment.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import vigi.patient.model.services.Treatment;


/**
 * Typical service for CRUD operations
 */
public interface TreatmentService {

    void init();

    boolean createTreatment(Treatment treatment);

    Treatment readTreatment(Long treatmentId);

    List<Treatment> readTreatments();

    List<Treatment> readTreatmentsWithCategory(String treatmentCategory);

    Treatment updateTreatment(Treatment treatment);

    boolean deleteTreatment(Long TreatmentId);
}
