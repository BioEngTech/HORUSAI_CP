package vigi.patient.presenter.service.appointment.impl;

import com.google.firebase.database.DataSnapshot;

import java.util.Map;
import java.util.UUID;

import vigi.patient.model.services.Appointment;

public final class AppointmentConverter {

    public static Appointment getAppointmentFromDataSnapshot(DataSnapshot snapshot) {

        Map<String, String> snapshotMap = (Map<String, String>) snapshot.getValue();

        Appointment appointment = new Appointment();
        appointment.setDate(snapshotMap.get("date"));
        appointment.setCareProviderId(snapshotMap.get("careProviderId"));
        appointment.setPatientId(snapshotMap.get("patientId"));
        appointment.setTreatmentId(snapshotMap.get("treatmentId"));
        appointment.setStatus(snapshotMap.get("status"));
        appointment.setPrice(snapshotMap.get("price"));

        return appointment;
    }

}
