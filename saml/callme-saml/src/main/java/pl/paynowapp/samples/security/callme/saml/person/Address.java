package pl.paynowapp.samples.security.callme.saml.person;
//import java.time.LocalDate;
import java.util.Date;

public class Address {
    private Address2 address;
    private Type type;
    private Date startOn;
    private String preference;

    public Address2 getAddress() {
        return address;
    }

    public void setAddress(Address2 address) {
        this.address = address;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getStartOn() {
        return startOn;
    }

    public void setStartOn(Date startOn) {
        this.startOn = startOn;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }
}