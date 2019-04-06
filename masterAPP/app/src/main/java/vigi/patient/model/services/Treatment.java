package vigi.patient.model.services;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

public class Treatment {

    private HashMap<String,Boolean> admittedjobs;
    private String image;
    private String expectedtime;
    private String pricehint;
    private String description;
    private String name;
    private String id;
    private String benefits;

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Treatment(Object admittedjobs, Object image, Object expectedtime, Object pricehint, Object description, Object name, Object id, Object benefits) {
        this.admittedjobs = (HashMap<String, Boolean>) admittedjobs;
        this.image = (String) image;
        this.expectedtime = (String) expectedtime;
        this.pricehint = (String) pricehint;
        this.description = (String) description;
        this.name = (String) name;
        this.id = (String) id;
        this.benefits = (String) benefits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Boolean> getAdmittedjobs() {
        return admittedjobs;
    }

    public void setAdmittedjobs(HashMap<String, Boolean> admittedjobs) {
        this.admittedjobs = admittedjobs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExpectedtime() {
        return expectedtime;
    }

    public void setExpectedtime(String expectedtime) {
        this.expectedtime = expectedtime;
    }

    public String getPricehint() {
        return pricehint;
    }

    public void setPricehint(String pricehint) {
        this.pricehint = pricehint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

