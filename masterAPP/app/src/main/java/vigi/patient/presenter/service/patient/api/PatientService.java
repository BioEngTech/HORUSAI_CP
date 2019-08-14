package vigi.patient.presenter.service.patient.api;

import android.content.Context;

import com.google.firebase.database.ValueEventListener;

import vigi.patient.model.entities.Patient;

/**
 * Typical service for CRUD operations
 */
public interface PatientService {

    void init();

    void readPatients(ValueEventListener listener);

    boolean createPatient(Patient patient);

    void readPatient(ValueEventListener valueEventListener, String patientId);

    void updatePatient(Context context, String patientKey, String parameter, String value);

    boolean deletePatient(Long patientId);

}
