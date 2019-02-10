package vigi.patient.mainclasses;

import java.util.ArrayList;

public class Service {

    private String description;
    private ArrayList<String> job;
    private String image;
    private String type;
    private ArrayList<String> cases;
   // private ArrayList<cfh> cases;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getJob() {
        return job;
    }

    public void setJob(ArrayList<String> job) {
        this.job = job;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getCases() {
        return cases;
    }

    public void setCases(ArrayList<String> cases) {
        this.cases = cases;
    }
}
