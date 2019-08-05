package vigi.patient.presenter.service.agenda.agenda.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import vigi.patient.model.entities.Agenda;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.presenter.service.agenda.agenda.api.AgendaService;

import static java.util.stream.Collectors.toList;

public class FirebaseAgendaService implements AgendaService{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceAgenda;
    List<String> careProviderIds, getCareProviderIdsAgenda;
    List<Integer> careProviderRatings;
    List<String> careProviderPrices;
    List<Agenda> allAgendas, allAgendasFiltered;
    private SimpleDateFormat sdfFullDate, sdfDate;
    private Comparator<CareProvider> careProviderComparator;


    @Override
    public void init() {
        allAgendas = new ArrayList<>();
        careProviderRatings = new ArrayList<>();
        careProviderPrices = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceAgenda = firebaseDatabase.getReference(Agenda.class.getSimpleName());
        sdfFullDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @Override
    public void readAgendas(ValueEventListener listener) {
        addOnOperationCompleteListener(listener);
    }

    @Override
    public List<Agenda> readAgendaWithFilter(String filter, List<CareProvider> careProvidersWithTreatment) {
        allAgendasFiltered = new ArrayList<>();

        if (filter.equals("Rating")){
            careProviderComparator = Comparator.comparing(CareProvider::getRating);
            Collections.sort(careProvidersWithTreatment, careProviderComparator.reversed());
        }else if (filter.equals("Price")){
            careProviderComparator = Comparator.comparing(CareProvider::getPrice);
            Collections.sort(careProvidersWithTreatment, careProviderComparator.reversed());
        }

        careProviderIds = careProvidersWithTreatment.stream().map(agenda -> agenda.getId()).collect(toList());

        // streaming the ID array in order
        String[] resultArray = new String[careProviderIds.size()];
        Arrays.stream(careProviderIds.toArray(resultArray))
                .forEach((i) -> {

                            // retrieving a Brand with same ID as the current
                            // from the "randomly" ordered list
                            Optional<Agenda> toAdd = allAgendas.stream()
                                    .filter((b) -> b.getCareProviderId().equals(String.valueOf(i)))
                                    .findFirst();

                            // making sure there's one
                            if (toAdd.isPresent()) {
                                // adding to linked set
                                allAgendasFiltered.add(toAdd.get());
                            }
                        }
                );

        return allAgendasFiltered;

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
