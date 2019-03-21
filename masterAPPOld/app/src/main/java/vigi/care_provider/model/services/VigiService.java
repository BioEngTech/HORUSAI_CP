package vigi.care_provider.model.services;

import android.graphics.Bitmap;

import java.util.List;

//TODO: CHANGE NAME!!!
public class VigiService {

    private String description;
    private String type;
    private List<String> job;
    private List<String> cases;
    private String imageUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getJob() {
        return job;
    }

    public void setJob(List<String> job) {
        this.job = job;
    }

    public List<String> getCases() {
        return cases;
    }

    public void setCases(List<String> cases) {
        this.cases = cases;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
