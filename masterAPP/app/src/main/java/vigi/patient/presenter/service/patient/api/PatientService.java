package vigi.patient.presenter.service.patient.api;

import com.google.firebase.database.ValueEventListener;

import vigi.patient.model.entities.Patient;

/**
 * Typical service for CRUD operations
 */
public interface PatientService {

    void init();

    void readPatients(ValueEventListener listener);

    boolean createPatient(Patient patient);

    Patient readPatient(Long patientId);

    Patient updatePatient(Long patientId);

    boolean deletePatient(Long patientId);

}
