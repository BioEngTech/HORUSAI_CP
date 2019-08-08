package vigi.patient.model.entities;

import java.io.Serializable;
import java.util.HashMap;

public class Agenda implements Serializable {

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

    public HashMap<String, Boolean> getCartDate() {
        return cartDate;
    }

    public void setCartDate(HashMap<String, Boolean> cartDate) {
        this.cartDate = cartDate;
    }

    HashMap<String, Boolean> cartDate;



}
