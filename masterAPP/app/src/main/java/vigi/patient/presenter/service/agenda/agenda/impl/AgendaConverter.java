package vigi.patient.presenter.service.agenda.agenda.impl;

import com.google.firebase.database.DataSnapshot;

import java.util.Map;

import vigi.patient.model.entities.Agenda;

public final class AgendaConverter {

    public static Agenda getAgendaFromDataSnapshot(DataSnapshot snapshot) {

        Agenda agenda = new Agenda();

        Map<String, String> snapshotMap = (Map<String, String>) snapshot.getValue();

        agenda.setEndDate(snapshotMap.get("endDate"));
        agenda.setCareProviderId(snapshotMap.get("careProviderId"));
        agenda.setStartDate(snapshotMap.get("startDate"));

        return agenda;
    }

}
