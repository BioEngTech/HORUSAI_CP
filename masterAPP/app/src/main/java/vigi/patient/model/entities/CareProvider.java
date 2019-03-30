package vigi.patient.model.entities;

import java.util.HashMap;

public class CareProvider {

    private String name;
    private String latitude;
    private String longitude;
    private String actionRay;
    private String rating;
    private String image;
    private String job;
    private HashMap<String, Boolean> appointments;
    private String legalId;

    public CareProvider(String name, String latitude, String longitude, String actionRay, String rating, String image, String job, HashMap<String, Boolean> appointments, String legalId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.actionRay = actionRay;
        this.rating = rating;
        this.image = image;
        this.job = job;
        this.appointments = appointments;
        this.legalId = legalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getActionRay() {
        return actionRay;
    }

    public void setActionRay(String actionRay) {
        this.actionRay = actionRay;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public HashMap<String, Boolean> getAppointments() {
        return appointments;
    }

    public void setAppointments(HashMap<String, Boolean> appointments) {
        this.appointments = appointments;
    }

    public String getLegalId() {
        return legalId;
    }

    public void setLegalId(String legalId) {
        this.legalId = legalId;
    }
}
