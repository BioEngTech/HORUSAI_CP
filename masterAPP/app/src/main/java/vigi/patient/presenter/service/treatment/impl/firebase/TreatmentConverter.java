package vigi.patient.presenter.service.treatment.impl.firebase;

import com.google.firebase.database.DataSnapshot;

import vigi.patient.model.services.Treatment;

public final class TreatmentConverter {

    public static Treatment getTreatmentFromDataSnapshot(DataSnapshot snapshot) {

        Treatment treatment = new Treatment();
        //TODO: Probably some fields fail since in Firebase might not be supported
        treatment.setName(snapshot.getValue(Treatment.class).getName());
        treatment.setBenefits(snapshot.getValue(Treatment.class).getBenefits());
        treatment.setCategory(snapshot.getValue(Treatment.class).getCategory());
        treatment.setDescription(snapshot.getValue(Treatment.class).getDescription());
        treatment.setExpectedMinutes(snapshot.getValue(Treatment.class).getExpectedMinutes());
        treatment.setImage(snapshot.getValue(Treatment.class).getImage());
        treatment.setPriceHint(snapshot.getValue(Treatment.class).getPriceHint());
        treatment.setAdmittedJobs(snapshot.getValue(Treatment.class).getAdmittedJobs());

        return treatment;
    }
}
