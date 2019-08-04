package vigi.patient.presenter.service.careProvider.api;

import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import vigi.patient.model.entities.Agenda;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Treatment;


/**
 * Typical service for CRUD operations
 */
public interface CareProviderService {

    void init();

    void readCareProviders(ValueEventListener listener);

    void setAllCareProviders(List<CareProvider> allCareProviders);

    List<CareProvider> readCareProviderWithTreatment(String job);

    CareProvider readCareProvider(UUID careProviderId);

    void addOnOperationCompleteListener(ValueEventListener valueEventListener);
}
