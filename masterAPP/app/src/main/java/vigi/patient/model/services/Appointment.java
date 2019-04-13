package vigi.patient.model.services;

import android.graphics.drawable.Drawable;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class Appointment {

    private Date date;
    private String report;
    private Integer rating;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Duration duration;
    private BigDecimal price;
    private UUID treatmentId;
    private UUID careProviderId;
    private UUID patientId;
    private String status;


    public Appointment() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UUID getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(UUID treatmentId) {
        this.treatmentId = treatmentId;
    }

    public UUID getCareProviderId() {
        return careProviderId;
    }

    public void setCareProviderId(UUID careProviderId) {
        this.careProviderId = careProviderId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
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