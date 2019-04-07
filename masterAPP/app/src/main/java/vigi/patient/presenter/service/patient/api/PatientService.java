package vigi.patient.presenter.service.patient.api;

import vigi.patient.model.entities.Patient;

/**
 * Typical service for CRUD operations
 */
public interface PatientService {

    void init();

    boolean createPatient(Patient patient);

    Patient readPatient(Long patientId);

    Patient updatePatient(Long patientId);

    boolean deletePatient(Long patientId);

}
