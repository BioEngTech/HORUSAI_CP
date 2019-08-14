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
        Patient.Position position = new Patient.Position();

        Map<String, String> snapshotMap = (Map<String, String>) snapshot.getValue();

        patient.setImage(snapshotMap.get("image"));
        patient.setName(snapshotMap.get("name"));
        patient.setId(snapshotMap.get("tokenid"));
        patient.setPayment(snapshotMap.get("payment"));

        position.setCity(snapshotMap.get("city"));
        position.setCountry(snapshotMap.get("country"));
        position.setNumber(Integer.parseInt(snapshotMap.get("number")));
        position.setStreet(snapshotMap.get("street"));

        patient.setPosition(position);

        return patient;
    }
}
