package pl.paynowapp.samples.security.callme.saml.address;

public class Country {
    private String code;
    private String title;
    private String postalTitle;
    private Region region;
    private String locality;
    private String postalCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostalTitle() {
        return postalTitle;
    }

    public void setPostalTitle(String postalTitle) {
        this.postalTitle = postalTitle;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
