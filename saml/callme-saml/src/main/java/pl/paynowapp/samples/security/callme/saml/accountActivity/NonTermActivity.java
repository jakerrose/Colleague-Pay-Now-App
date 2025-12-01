package pl.paynowapp.samples.security.callme.saml.accountActivity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class NonTermActivity
{
    @JsonProperty("AssociatedPeriods")
    private ArrayList<Object> associatedPeriods;
    @JsonProperty("Balance")
    private int balance;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("StartDate")
    private Object startDate;
    @JsonProperty("EndDate")
    private Object endDate;

    public ArrayList<Object> getAssociatedPeriods() {
        return associatedPeriods;
    }
    public void setAssociatedPeriods(ArrayList<Object> associatedPeriods) {
        this.associatedPeriods = associatedPeriods;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
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
    public Object getStartDate() {
        return startDate;
    }
    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }
    public Object getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
