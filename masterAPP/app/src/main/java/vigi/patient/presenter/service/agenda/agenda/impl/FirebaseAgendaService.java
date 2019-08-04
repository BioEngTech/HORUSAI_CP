package vigi.patient.presenter.service.agenda.agenda.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import vigi.patient.model.entities.Agenda;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.presenter.service.agenda.agenda.api.AgendaService;

import static java.util.stream.Collectors.toList;

public class FirebaseAgendaService implements AgendaService{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceAgenda;
    List<String> careProviderIds;
    List<Agenda> allAgendas;
    private SimpleDateFormat sdfFullDate, sdfDate;

    @Override
    public void init() {
        allAgendas = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceAgenda = firebaseDatabase.getReference(Agenda.class.getSimpleName());
        sdfFullDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    }

    @Override
    public void readAgendas(ValueEventListener listener) {
        addOnOperationCompleteListener(listener);
    }

    @Override
    public List<Agenda> readAgendaWithFilter(String filter) {

        /*if (filter.equals("Rating")){
            Log.d("NAMASTE filter", "Rating");

            return allCareProviders.stream()
                    .sorted(Comparator.comparing(CareProvider::getRating))
                    .collect(toList());
        }else if (filter.equals("Hour")){
            return null;
        }else if (filter.equals("Price")){
            Log.d("NAMASTE filter", "Price");

            return allCareProviders.stream()
                    .sorted(Comparator.comparing(CareProvider::getPrice))
                    .collect(toList());
        }*/
        return null;
    }

    @Override
    public List<Agenda> readAgendaWithDate(String date) {
        //TODO set string to date in utils

        Date startDate = null;
        try {
            startDate = sdfFullDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date finalStartDate = startDate;
        return allAgendas.stream()
                .filter(agendaInstance -> {
                    try {
                        return finalStartDate
                                .after(sdfFullDate.parse(agendaInstance.getStartDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return Boolean.parseBoolean(null);
                    }
                })
                .filter(agendaInstance -> {
                    try {
                        //TODO add duration to startDate
                        return finalStartDate
                                .before(sdfFullDate.parse(agendaInstance.getEndDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return Boolean.parseBoolean(null);
                    }
                })
                .collect(toList());
    }

    @Override
    public void addOnOperationCompleteListener(ValueEventListener valueEventListener) {
        databaseReferenceAgenda.addValueEventListener(valueEventListener);
    }

    @Override
    public void setAllAgendas(List<Agenda> allAgendas) {
        this.allAgendas = allAgendas;
    }
}
