package vigi.patient.model.entities;


import java.util.HashMap;

/**
 * Model POJO (plain old java object)
 * for the patient user
 */
public class Patient {

    //TODO: Add birthday date

    private String id;
    private String name;
    private String image;
    private HashMap<String, Boolean> appointments;

    private String latitude;
    private String longitude;

    public HashMap<String, Boolean> getAppointments() {
        return appointments;
    }

    public void setAppointments(HashMap<String, Boolean> appointments) {
        this.appointments = appointments;
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

    private String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}