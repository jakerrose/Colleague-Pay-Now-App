package pl.paynowapp.samples.security.callme.saml.accountActivity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Periods {
    @JsonProperty("AssociatedPeriods")
    private ArrayList<String> associatedPeriods;
    @JsonProperty("Balance")
    private BigDecimal balance;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;

    public ArrayList<String> getAssociatedPeriods() {
        return associatedPeriods;
    }
    public void setAssociatedPeriods(ArrayList<String> associatedPeriods) {
        this.associatedPeriods = associatedPeriods;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
