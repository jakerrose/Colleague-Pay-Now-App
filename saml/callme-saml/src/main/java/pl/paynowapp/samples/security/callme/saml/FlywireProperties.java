package pl.paynowapp.samples.security.callme.saml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flywire")
public class FlywireProperties {
    private String mode; // "sandbox" or "production"

    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }

    // URLs derived from mode
    public String getSdkUrl() {
        return isSandbox()
                ? "https://artifacts.flywire.com/sdk/js/v0/sandbox.main.js"
                : "https://artifacts.flywire.com/sdk/js/v0/main.js";
    }

    public String getPaymentUrl() {
        return isSandbox()
                ? "https://api-platform-sandbox.flywire.com/payments/v1/payments/"
                : "https://api-platform.flywire.com/payments/v1/payments/";
    }

    public String getCheckoutUrl() {
        return isSandbox()
                ? "https://api-platform-sandbox.flywire.com/payments/v1/checkout/sessions"
                : "https://api-platform.flywire.com/payments/v1/checkout/sessions";
    }

    private boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(mode);
    }
}

