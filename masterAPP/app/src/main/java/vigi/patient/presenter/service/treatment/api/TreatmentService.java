package vigi.patient.presenter.service.treatment.api;

import java.util.List;
import java.util.UUID;

import vigi.patient.model.services.Treatment;


/**
 * Typical service for CRUD operations
 */
public interface TreatmentService {

    void init();

    boolean createTreatment(Treatment treatment);

    Treatment readTreatment(UUID treatmentId);

    List<Treatment> readTreatments();

    List<Treatment> readTreatmentsWithCategory(String treatmentCategory);

    Treatment updateTreatment(Treatment treatment);

    boolean deleteTreatment(UUID TreatmentId);
}
