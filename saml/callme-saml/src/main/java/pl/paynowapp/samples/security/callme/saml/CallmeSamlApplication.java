package pl.paynowapp.samples.security.callme.saml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

@EnableConfigurationProperties
@SpringBootApplication
public class CallmeSamlApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallmeSamlApplication.class, args);

        // Open browser after app starts
        String url = "https://localhost:7171/index";
        openBrowser(url);
    }

    private static void openBrowser(String url) {
        try {
            // Use Desktop to launch browser
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
            } else {
                // Fallback for Windows
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec(new String[]{"open", url});
                } else if (os.contains("nix") || os.contains("nux")) {
                    Runtime.getRuntime().exec(new String[]{"xdg-open", url});
                } else {
                    System.err.println("No supported method to open browser");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to open browser: " + e.getMessage());
        }
    }
}
