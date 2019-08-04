package vigi.patient.model.entities;

public class Agenda {

    public String getCareProviderId() {
        return careProviderId;
    }

    public void setCareProviderId(String careProviderId) {
        this.careProviderId = careProviderId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    String careProviderId, endDate, startDate;


}
