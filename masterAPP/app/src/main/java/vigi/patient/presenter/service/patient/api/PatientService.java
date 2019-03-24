package vigi.patient.presenter.service.patient.api;

import java.util.concurrent.ExecutionException;

import vigi.patient.model.entities.Patient;

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
