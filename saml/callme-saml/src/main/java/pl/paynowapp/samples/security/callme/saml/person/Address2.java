package pl.paynowapp.samples.security.callme.saml.person;

public class Address2 {
    private String id;


    public Address2(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Address2{" +
                "id='" + id + '\'' +
                '}';
    }
public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
