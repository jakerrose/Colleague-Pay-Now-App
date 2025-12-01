package pl.paynowapp.samples.security.callme.saml;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import pl.paynowapp.samples.security.callme.saml.accountActivity.AccountActivity;
import pl.paynowapp.samples.security.callme.saml.address.Address;
import pl.paynowapp.samples.security.callme.saml.paymentsResult.PaymentsResult;
import pl.paynowapp.samples.security.callme.saml.person.Person;
import pl.paynowapp.samples.security.callme.saml.person.*;

@RestController
@RequestMapping("/api/flywire")
public class FlywireController {

    private static final Logger logger = LoggerFactory.getLogger(FlywireController.class);

    private final FlywireService flywireService;
    private final ColleagueStudentService colleagueStudentService;
    private final ColleagueProperties colleagueProperties;
    @Autowired
    private AppProperties appProperties;

    @Autowired
    public FlywireController(FlywireService flywireService, ColleagueStudentService colleagueStudentService, ColleagueProperties colleagueProperties) {
        this.flywireService = flywireService;
        this.colleagueStudentService = colleagueStudentService;
        this.colleagueProperties = colleagueProperties;
    }

    @PostMapping("/session")
    public ResponseEntity<FlywireSessionResponse> postFlywireSession(
            @AuthenticationPrincipal Saml2AuthenticatedPrincipal principal,
            @RequestParam(name = "inputAmt") Integer inputAmt,
            @RequestParam(name = "inputEmail") String inputEmail
    ) {

        logger.info("POST request for Flywire session with inputAmt={}", inputAmt);
        logger.info("POST request for Flywire session with emailAmt={}", inputEmail);

        if (inputAmt == null) {
            logger.warn("inputAmt is missing from POST request");
            return ResponseEntity.badRequest().build();
        }

        try {
            String studentId = principal.getFirstAttribute("student_id");
            String firstName = null;
            String lastName = null;
            String email = null;
            BigDecimal rawAccountBalance = null;
            String accountBalance = "0.00"; // Default balance if not found
            String address = null;
            String city = null;
            String state = null;
            String zip = null;
            String country = null;
            String phone = null;
            String addressId = null;
            String termCode = null;

            Person person = colleagueStudentService.getStudentByColleagueId(studentId);
            if (person != null) {
                try {
                    Optional<Name> configuredName = colleagueStudentService.getConfiguredName(person);
                    if (configuredName.isPresent()) {
                        firstName = configuredName.get().getFirstName();
                        lastName = configuredName.get().getLastName();
                        logger.info("Using configured name: " + firstName + " " + lastName);
                    } else {
                        logger.warn("No name found for type: "+ colleagueProperties.getNameType());
                        if(!person.getNames().isEmpty()){
                            firstName = person.getNames().get(0).getFirstName();
                            lastName = person.getNames().get(0).getLastName();
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.info("No names found");
                    firstName = null;
                    lastName = null;
                }

            email = inputEmail;
                
                try {
                    Optional<Phone> configuredPhone = colleagueStudentService.getConfiguredPhone(person);
                    if (configuredPhone.isPresent()) {
                        phone = configuredPhone.get().getNumber();
                        //add 1 for country code
                        phone = "+" + 1 + " " + phone;
                        logger.info("Using configured phone: {}", phone);
                    } else {
                        logger.warn("No phone found for type: {}", colleagueProperties.getPhoneType());
                        // fallback: maybe use first phone or handle gracefully
                        if (!person.getPhones().isEmpty()) {
                            String fallbackPhone = person.getPhones().get(0).getNumber();
                            //add 1 for country code
                            phone = "+" + 1 + " " + fallbackPhone;
                            logger.info("Falling back to first phone: {}", phone);
                        }
                    }
                }
                catch (Exception e) {
                    logger.info("No phone found");
                    phone = null;
                }

                addressId = person.getAddresses().get(0).getAddress().getId();
                logger.info("addressId: {}", addressId);
                // Fetch address details using addressId
                if (addressId != null) {
                    Address fullAddress = colleagueStudentService.getAddressByAddressId(addressId);
                    if (fullAddress != null) {
                        address = fullAddress.getAddressLines().get(0);  // You need to make sure this field exists
                        city = fullAddress.getPlace().getCountry().getLocality();
                        state = fullAddress.getPlace().getCountry().getRegion().getTitle();
                        if ("California".equals(state)) {
                            state = "CA";
                        } else if ("New York".equals(state)) {
                            state = "NY";
                        } else if ("Texas".equals(state)) {
                            state = "TX";
                        } else if ("Florida".equals(state)) {
                            state = "FL";
                        } else if ("Illinois".equals(state)) {
                            state = "IL";
                        } else if ("Pennsylvania".equals(state)) {
                            state = "PA";
                        } else if ("Ohio".equals(state)) {
                            state = "OH";
                        } else if ("Georgia".equals(state)) {
                            state = "GA";
                        } else if ("North Carolina".equals(state)) {
                            state = "NC";
                        } else if ("Michigan".equals(state)) {
                            state = "MI";
                        } else if ("New Jersey".equals(state)) {
                            state = "NJ";
                        } else if ("Virginia".equals(state)) {
                            state = "VA";
                        } else if ("Washington".equals(state)) {
                            state = "WA";
                        } else if ("Arizona".equals(state)) {
                            state = "AZ";
                        } else if ("Massachusetts".equals(state)) {
                            state = "MA";
                        } else if ("Tennessee".equals(state)) {
                            state = "TN";
                        } else if ("Indiana".equals(state)) {
                            state = "IN";
                        } else if ("Missouri".equals(state)) {
                            state = "MO";
                        } else if ("Maryland".equals(state)) {
                            state = "MD";
                        } else if ("Wisconsin".equals(state)) {
                            state = "WI";
                        } else if ("Colorado".equals(state)) {
                            state = "CO";
                        } else if ("Minnesota".equals(state)) {
                            state = "MN";
                        } else if ("South Carolina".equals(state)) {
                            state = "SC";
                        } else if ("Alabama".equals(state)) {
                            state = "AL";
                        } else if ("Kentucky".equals(state)) {
                            state = "KY";
                        } else if ("Oregon".equals(state)) {
                            state = "OR";
                        } else if ("Oklahoma".equals(state)) {
                            state = "OK";
                        } else if ("Connecticut".equals(state)) {
                            state = "CT";
                        } else if ("Iowa".equals(state)) {
                            state = "IA";
                        } else if ("Mississippi".equals(state)) {
                            state = "MS";
                        } else if ("Arkansas".equals(state)) {
                            state = "AR";
                        } else if ("Kansas".equals(state)) {
                            state = "KS";
                        } else if ("Utah".equals(state)) {
                            state = "UT";
                        } else if ("Nevada".equals(state)) {
                            state = "NV";
                        } else if ("New Mexico".equals(state)) {
                            state = "NM";
                        } else if ("West Virginia".equals(state)) {
                            state = "WV";
                        } else if ("Nebraska".equals(state)) {
                            state = "NE";
                        } else if ("Idaho".equals(state)) {
                            state = "ID";
                        } else if ("Hawaii".equals(state)) {
                            state = "HI";
                        } else if ("Maine".equals(state)) {
                            state = "ME";
                        } else if ("New Hampshire".equals(state)) {
                            state = "NH";
                        } else if ("Rhode Island".equals(state)) {
                            state = "RI";
                        } else if ("Delaware".equals(state)) {
                            state = "DE";
                        } else if ("Montana".equals(state)) {
                            state = "MT";
                        } else if ("Wyoming".equals(state)) {
                            state = "WY";
                        } else if ("South Dakota".equals(state)) {
                            state = "SD";
                        } else if ("North Dakota".equals(state)) {
                            state = "ND";
                        } else if ("Alaska".equals(state)) {
                            state = "AK";
                        } else if ("Vermont".equals(state)) {
                            state = "VT";
                        }

                        zip = fullAddress.getPlace().getCountry().getPostalCode();
                        country = fullAddress.getPlace().getCountry().getCode();
                        if ("USA".equals(country)) {
                            country = "US";
                        }
                        // Use or log these values as needed
                        logger.info("Student address: {}, {}, {} {}", address, city, state, zip, country);
                    }
                    try {
                        AccountActivity balance = colleagueStudentService.getBalancebyStudentId(studentId);

                        if (balance != null) {
                            rawAccountBalance = balance.getPeriods().get(0).getBalance();
                            // Build locale from config
                            Locale locale = new Locale(appProperties.getLanguage(), appProperties.getCountryCode());
                            // Format currency
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                            accountBalance = currencyFormatter.format(rawAccountBalance);
                            accountBalance = accountBalance.contains(".") ? accountBalance : accountBalance + ".00";
                            termCode = String.valueOf(balance.getPeriods().get(0).getAssociatedPeriods().get(0));
                        }
                        else {
                            accountBalance = "0.00";
                        }
                    }
                    catch (Exception e)
                    {
                        logger.info("No terms found");
                        accountBalance = null;
                    }
                }
            }
            FlywireSessionResponse response = flywireService.generateSession(
                    firstName, lastName, email, studentId, accountBalance, address, city, state, zip, country, phone, inputAmt, termCode, inputEmail);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating Flywire session (POST)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

@PostMapping("/payment-result")
public ResponseEntity<Map<String, String>> handleFlywireResult(@RequestBody FlywirePaymentResult result,
                                                               HttpSession session) {
    String confirmUrl = result.getConfirm_url().getUrl();

    // Call Flywire confirmSession, which returns paymentReference
    String paymentReference = flywireService.confirmSession(confirmUrl);

    // Store in session for the /success page
    session.setAttribute("reference", paymentReference);
    Map<String, String> resp = new HashMap<>();
    resp.put("paymentReference", paymentReference);
    return ResponseEntity.ok(resp);
}
@PostMapping("/payments")
public ResponseEntity<Map<String, String>> handleFlywirePayments(@RequestBody PaymentsResult response,
                                                HttpSession session) {
    logger.info("PaymentsResponse.id: {}", response.getPayment_id());
        String referenceId = response.getPayment_id();
        logger.info("Received Flywire payment reference: {}", referenceId);
        //call getTrackingId method to get tracking URL
        String trackingUrl = flywireService.getTrackingId(referenceId);

    // Store in session for the /success page
        session.setAttribute("tracking_url", trackingUrl);
        logger.info("trackingUrl: {}", trackingUrl);
        //return ResponseEntity.ok().build();
    Map<String, String> resp = new HashMap<>();
    resp.put("tracking_url", trackingUrl);
    return ResponseEntity.ok(resp);
}


    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        model.addAttribute("reference", session.getAttribute("reference"));
        String trackingUrl = (String) session.getAttribute("tracking_url");
        model.addAttribute("tracking_url", trackingUrl);
        return "success";
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody FlywireWebhookPayload payload,
                                                @RequestHeader Map<String, String> headers) {
        logger.info("Received Flywire webhook:");
        logger.info("Headers: {}", headers);
        logger.info("Payload: {}", payload);

        return ResponseEntity.ok("Webhook received");
    }
}


