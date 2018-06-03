package com.example.daniel.seriousapp.JMS;

import com.example.daniel.seriousapp.utils.EventScenario;
import com.example.daniel.seriousapp.utils.LocationWrapper;

import java.io.Serializable;

/**
 * Created by joaosousa on 03/06/18.
 */
class JMSObject implements Serializable {
    private LocationWrapper location;
    private EventScenario eventScenario;
    private String patientBackground;
    private String extraNotes;

    JMSObject(LocationWrapper location, EventScenario eventScenario,
              String patientBackground, String extraNotes) {
        this.location = location;
        this.eventScenario = eventScenario;
        this.patientBackground = patientBackground;
        this.extraNotes = extraNotes;
    }

    public LocationWrapper getLocation() {
        return location;
    }

    public void setLocation(LocationWrapper location) {
        this.location = location;
    }

    public EventScenario getEventScenario() {
        return eventScenario;
    }

    public void setEventScenario(EventScenario eventScenario) {
        this.eventScenario = eventScenario;
    }

    public String getPatientBackground() {
        return patientBackground;
    }

    public void setPatientBackground(String patientBackground) {
        this.patientBackground = patientBackground;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public void setExtraNotes(String extraNotes) {
        this.extraNotes = extraNotes;
    }
}
