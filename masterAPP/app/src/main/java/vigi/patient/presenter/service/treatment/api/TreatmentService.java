package vigi.patient.presenter.service.treatment.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import vigi.patient.model.services.Treatment;

/**
 * Typical service for CRUD operations
 */
public interface TreatmentService {

    public void onStart();
    public void onSuccess(DataSnapshot data);
    public void onFailed(DatabaseError databaseError);

}
