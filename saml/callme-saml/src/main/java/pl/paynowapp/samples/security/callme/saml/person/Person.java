package pl.paynowapp.samples.security.callme.saml.person;


import java.util.List;

public class Person {
    private List<Name> names;
    private String dateOfBirth;
    private String gender;
    private Gendermarker gendermarker;
    private List<Role> roles;
    private List<Credential> credentials;
    private List<Address> addresses;
    private List<Phone> phones;
    private List<Email> emails;
    private String id;
    private Permil permil;


    // Getters and setters
    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public Gendermarker getGendermarker() {
        return gendermarker;
    }
    public void setGendermarker(Gendermarker gendermarker) {
        this.gendermarker = gendermarker;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Permil getPermil() {
        return permil;
    }
    public void setPermil(Permil permil) {
        this.permil = permil;
    }
}
