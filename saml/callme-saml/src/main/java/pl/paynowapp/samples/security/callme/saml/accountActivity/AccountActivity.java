package pl.paynowapp.samples.security.callme.saml.accountActivity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Map;

public class AccountActivity {
    @JsonProperty("Periods")
    private ArrayList<Periods> periods;
    @JsonProperty("NonTermActivity")
    private Map<String,Object> nonTermActivity;

    public String getNonTermActivity() {
        return nonTermActivity != null ? nonTermActivity.toString() : "";
    }
        public void setNonTermActivity(Map<String, Object> nonTermActivity) {
            this.nonTermActivity = nonTermActivity;
        }
    public ArrayList<Periods> getPeriods() {
        return periods;
    }
    public void setPeriods(ArrayList<Periods> periods) {
        this.periods = periods;
    }
}