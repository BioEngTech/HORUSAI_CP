package vigi.patient.model.services;

import android.graphics.drawable.Drawable;

public class Appointment {

    private String hour;
    private String paymentCode;
    private String paymentStatus;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    private String day;
    private String duration;

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    private String price;
    private String treatment;
    // TODO private String careProviderId;
    private String careProviderName;
    private String careProviderRating;
    private Drawable careProviderImage;
    // TODO private String patientId;
    private String patientName;
    private String patientAge;
    private Drawable patientImage;
    // TODO position of the patient and careprovider should also be add


    public Appointment(String hour, String paymentCode, String paymentStatus, String day, String duration, String price, String treatment, String careProviderName, String careProviderRating, Drawable careProviderImage, String patientName, String patientAge, Drawable patientImage) {
        this.hour = hour;
        this.paymentCode = paymentCode;
        this.paymentStatus = paymentStatus;
        this.day = day;
        this.duration = duration;
        this.price = price;
        this.treatment = treatment;
        this.careProviderName = careProviderName;
        this.careProviderRating = careProviderRating;
        this.careProviderImage = careProviderImage;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientImage = patientImage;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getCareProviderName() {
        return careProviderName;
    }

    public void setCareProviderName(String careProviderName) {
        this.careProviderName = careProviderName;
    }

    public String getCareProviderRating() {
        return careProviderRating;
    }

    public void setCareProviderRating(String careProviderRating) {
        this.careProviderRating = careProviderRating;
    }

    public Drawable getCareProviderImage() {
        return careProviderImage;
    }

    public void setCareProviderImage(Drawable careProviderImage) {
        this.careProviderImage = careProviderImage;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public Drawable getPatientImage() {
        return patientImage;
    }

    public void setPatientImage(Drawable patientImage) {
        this.patientImage = patientImage;
    }
}
