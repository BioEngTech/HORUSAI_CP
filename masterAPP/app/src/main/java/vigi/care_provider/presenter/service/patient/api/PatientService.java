package vigi.care_provider.presenter.service.patient.api;

import java.util.concurrent.ExecutionException;

import vigi.care_provider.model.entities.Patient;

/**
 * Typical service for CRUD operations
 */
public interface PatientService {

    void init();

    boolean createPatient(Patient patient);

    Patient readPatient();

    Patient updatePatient();

    boolean deletePatient();

}
