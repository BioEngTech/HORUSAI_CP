package vigi.patient.model.services;

import android.graphics.drawable.Drawable;

public class Treatment {

    private String id;
    private String title;
    private Drawable image;
    private String category;
    private String description;
    private String duration;
    private String benefits;

    public Treatment(String id, String title, Drawable image, String category, String description, String duration, String benefits) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.category = category;
        this.description = description;
        this.duration = duration;
        this.benefits = benefits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }
}

