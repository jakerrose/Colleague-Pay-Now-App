package pl.paynowapp.samples.security.callme.saml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "colleague")
public class ColleagueProperties {

    private String addressType;
    private String emailType;
    private String phoneType;
    private String nameType;

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
    public String getPhoneType() {
        return phoneType;
    }
    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }
    public String getNameType() {
        return nameType;
    }
    public void setNameType(String nameType) {
        this.nameType = nameType;
    }
}

