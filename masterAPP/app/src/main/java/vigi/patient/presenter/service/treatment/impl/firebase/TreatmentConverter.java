package vigi.patient.presenter.service.treatment.impl.firebase;

import com.google.firebase.database.DataSnapshot;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vigi.patient.model.services.Treatment;

public final class TreatmentConverter {

    public static Treatment getTreatmentFromDataSnapshot(DataSnapshot snapshot) {

        Treatment treatment = new Treatment();

        Map<String, String> snapshotMap = (Map<String, String>) snapshot.getValue();

        //TODO: Probably some fields fail since in Firebase might not be supported
        treatment.setName(snapshotMap.get("name"));
        treatment.setBenefits(snapshotMap.get("benefits"));
        treatment.setCategory(Treatment.TreatmentCategory.valueOf(snapshotMap.get("category")));
        treatment.setDescription(snapshotMap.get("description"));
        treatment.setMinutesOfDuration(Integer.parseInt(snapshotMap.get("expectedtime")));

        try {
            treatment.setImage(new URL(snapshotMap.get("image")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        treatment.setPriceHint(new BigDecimal(snapshotMap.get("pricehint")));

        List<String> admittedjobs = (List<String>) ((HashMap)snapshot.getValue()).get("admittedjobs");
        treatment.setAdmittedJobs(admittedjobs);

        return treatment;
    }
}
