package vigi.patient.model.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class Appointment {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    private String report;
    private Integer rating;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Integer minutesOfDuration;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;
    private String treatmentId;
    private String careProviderId;
    private String patientId;
    private String status;

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getCareProviderId() {
        return careProviderId;
    }

    public void setCareProviderId(String careProviderId) {
        this.careProviderId = careProviderId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Appointment() {
    }

    public Appointment(String date, String report, Integer rating, Double latitude, Double longitude, Double altitude, Integer minutesOfDuration, String price, String treatmentId, String careProviderId, String patientId, String status) {
        this.date = date;
        this.report = report;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.minutesOfDuration = minutesOfDuration;
        this.price = price;
        this.treatmentId = treatmentId;
        this.careProviderId = careProviderId;
        this.patientId = patientId;
        this.status = status;
    }


    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Integer getMinutesOfDuration() {
        return minutesOfDuration;
    }

    public void setMinutesOfDuration(Integer minutesOfDuration) {
        this.minutesOfDuration = minutesOfDuration;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public enum AppointmentStatus {
        CANCELLED("CANCELLED"),
        ONHOLD("ONHOLD"),
        ONGOING("ONGOING"),
        ACTIVE("ACTIVE");

        private String status;

        AppointmentStatus(String category) {
            this.status = status;
        }

        public String categoryString() {
            return status;
        }
    }

}