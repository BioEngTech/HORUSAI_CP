package vigi.patient.presenter.service.careProvider.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Treatment;

public final class CareProviderConverter {

    public static CareProvider getCareProviderFromDataSnapshot(DataSnapshot snapshot) {

        CareProvider careProvider = new CareProvider();

        Map<String, String> snapshotMap = (Map<String, String>) snapshot.getValue();

        //TODO: Probably some fields fail since in Firebase might not be supported
        careProvider.setName(snapshotMap.get("name"));
        careProvider.setId(snapshotMap.get("id"));
        careProvider.setExpectedtime(snapshotMap.get("expectedtime"));
        careProvider.setJob(snapshotMap.get("job"));
        careProvider.setPrice(snapshotMap.get("price"));
        careProvider.setRating((int)Float.parseFloat(snapshotMap.get("rating")));

        try {
            careProvider.setImage(new URL(snapshotMap.get("image")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //   careProvider.setPrice(new BigDecimal(snapshotMap.get("price")));

        return careProvider;
    }
}
