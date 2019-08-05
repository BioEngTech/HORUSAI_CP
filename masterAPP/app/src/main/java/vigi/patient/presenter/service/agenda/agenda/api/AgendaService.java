package vigi.patient.presenter.service.agenda.agenda.api;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vigi.patient.model.entities.Agenda;
import vigi.patient.model.entities.CareProvider;

public interface AgendaService {


    void init();

    void readAgendas(ValueEventListener listener);

    void setAllAgendas(List<Agenda> allAgendas);

    List<Agenda> readAgendaWithFilter(String filter, List<CareProvider> careProvidersWithTreatment);

    List<Agenda> readAgendaWithDate(String date);

    void addOnOperationCompleteListener(ValueEventListener valueEventListener);

}
