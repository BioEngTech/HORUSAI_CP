package vigi.patient.presenter.service.treatment.impl.firebase;

import android.util.Log;

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

        treatment.setName(snapshotMap.get("name"));
        treatment.setId(snapshotMap.get("id"));
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

        String admittedjobs = snapshotMap.get("admittedjobs");
        treatment.setAdmittedJobs(admittedjobs);

        return treatment;
    }
}
