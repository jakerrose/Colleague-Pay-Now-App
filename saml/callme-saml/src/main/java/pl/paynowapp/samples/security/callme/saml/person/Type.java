package pl.paynowapp.samples.security.callme.saml.person;

public class Type {
    private String phoneType;
    private Detail detail;
    private String category;
    private String addressType;
    private String emailType;

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getAddressType() {
        return addressType;
    }
    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
    public String getEmailType() {
        return emailType;
    }
    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}