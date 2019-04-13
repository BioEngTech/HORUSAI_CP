package vigi.patient.model.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Treatment implements Serializable {

    private UUID id;
    private String name;
    private String description;
    private TreatmentCategory category;
    private String benefits;
    private String[] admittedJobs;
    private URL image;
    private Duration expectedMinutes; /*e.g. Duration.ofSeconds(10)*/
    private BigDecimal priceHint;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TreatmentCategory getCategory() {
        return category;
    }

    public void setCategory(TreatmentCategory category) {
        this.category = category;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String[] getAdmittedJobs() {
        return admittedJobs;
    }

    public void setAdmittedJobs(String[] admittedJobs) {
        this.admittedJobs = admittedJobs;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public Duration getExpectedMinutes() {
        return expectedMinutes;
    }

    public void setExpectedMinutes(Duration expectedMinutes) {
        this.expectedMinutes = expectedMinutes;
    }

    public BigDecimal getPriceHint() {
        return priceHint;
    }

    public void setPriceHint(BigDecimal priceHint) {
        this.priceHint = priceHint;
    }

    public enum TreatmentCategory {
        DAILY_ASSISTANCE("Daily Assistance"),
        MEDICAL_ASSISTANCE("Medical Assistance");

        private String category;

        TreatmentCategory(String category) {
            this.category = category;
        }

        public String categoryString() {
            return category;
        }

        //TODO: Refactor (logic should not be calculated every time)
        public static List<String> getCategories() {
            List<String> categories = new ArrayList<>();
            for (TreatmentCategory category : TreatmentCategory.values()) {
                categories.add(category.categoryString());
            }
            return categories;
        }
    }
}

