package vigi.patient.presenter.service.patient.impl;

import com.google.firebase.database.DataSnapshot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.entities.Patient;

public final class PatientConverter {

    public static Patient getPatientFromDataSnapshot(DataSnapshot snapshot) {

        Patient patient = new Patient();

        Map<String, String> snapshotMap = (Map<String, String>) snapshot.getValue();

        patient.setImage(snapshotMap.get("image"));
        patient.setName(snapshotMap.get("name"));
        patient.setId(snapshotMap.get("tokenid"));

        return patient;
    }
}
