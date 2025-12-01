package pl.paynowapp.samples.security.callme.saml;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.paynowapp.samples.security.callme.saml.accountActivity.AccountActivity;
import pl.paynowapp.samples.security.callme.saml.address.Address;
import pl.paynowapp.samples.security.callme.saml.person.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@Controller
public class HomeController {


    private FlywireService flywireService;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final ColleagueStudentService colleagueStudentService;
    private final FlywireProperties flywireProperties;
    private final ColleagueProperties colleagueProperties;
    @Value("${api.public-key}")
    private String frontendKey;
    @Autowired
    private AppProperties appProperties;

    public HomeController(FlywireService flywireService,
                          ColleagueStudentService colleagueStudentService, FlywireProperties flywireProperties, ColleagueProperties colleagueProperties) {
        this.flywireService = flywireService;
        this.colleagueStudentService = colleagueStudentService;
        this.flywireProperties = flywireProperties;
        this.colleagueProperties = colleagueProperties;
    }
@GetMapping("/index")
public String index(Authentication authentication, Model model, @RequestParam(name = "inputAmt", required = false) Integer inputAmt, RedirectAttributes redirectAttributes) {

    if (authentication != null && authentication.getPrincipal() instanceof Saml2AuthenticatedPrincipal principal) {

        // DEBUG: Print all SAML attributes
        principal.getAttributes().forEach((key, value) -> {
            logger.info("SAML Attribute -> {} = {}", key, value);
        });

        String studentId = principal.getFirstAttribute("student_id");
        String firstName = null;
        String lastName = null;
        String email = null;
        BigDecimal rawAccountBalance = null;
        String accountBalance = "0.00";
        String address = null;
        String city = null;
        String state = null;
        String zip = null;
        String country = null;
        String phone = null;
        String addressId = null;
        String termCode = null;
        String sdkUrl = null;

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

                try {
                Optional<Email> configuredEmail = colleagueStudentService.getConfiguredEmail(person);

                    if (configuredEmail.isPresent()) {
                        email= configuredEmail.get().getAddress();
                        logger.info("Using configured email: {}", email);
                    } else {
                        logger.warn("No email found for type: {}", colleagueProperties.getEmailType());

                        // fallback example
                        if (!person.getEmails().isEmpty()) {
                            String fallbackEmail = person.getEmails().get(0).getAddress();
                            logger.info("Falling back to first email: {}", fallbackEmail);
                            email = fallbackEmail;
                        }
                    }
                }
                catch (Exception e) {
                    logger.info("No email found");
                    email = null;
                }
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

                try{
                Optional<Address> configuredAddress = colleagueStudentService.getConfiguredAddress(person);
                if (configuredAddress.isPresent()) {
                    addressId = configuredAddress.get().getId();
                    // now you can continue using addressId
                    logger.info("Using configured address ID: {}", addressId);
                } else {
                    logger.warn("No address found for type: {}", colleagueProperties.getAddressType());
                    // fallback: maybe use first address or handle gracefully
                    if (!person.getAddresses().isEmpty()) {
                        String fallbackAddressId = person.getAddresses().get(0).getAddress().getId();
                        logger.info("Falling back to first address ID: {}", fallbackAddressId);
                        addressId = fallbackAddressId;
                    }
                }

                logger.info("addressId: {}", addressId);
                // Fetch address details using addressId

                    Address fullAddress = colleagueStudentService.getAddressByAddressId(addressId);
                    if (fullAddress != null) {
                        address = fullAddress.getAddressLines().get(0);
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
                    }
                }
                    catch(Exception e)
                        {
                            address = "Unknown Address";
                            city = "Unknown City";
                            state = "Unknown State";
                            zip = "Unknown Zip";
                            country = "Unknown Country";
                        }
                        // Use or log these values as needed
                        logger.info("Student address: {}, {}, {} {}", address, city, state, zip, country);

                try {
                    AccountActivity balance = colleagueStudentService.getBalancebyStudentId(studentId);

                        if (balance != null) {
                            rawAccountBalance = balance.getPeriods().get(0).getBalance();
                            logger.info("Raw account balance: {}", accountBalance);
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
                    sdkUrl = flywireProperties.getSdkUrl();
            }

        model.addAttribute("frontendKey", frontendKey);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("student_id", studentId);
        model.addAttribute("email", email);
        model.addAttribute("account_balance", accountBalance);
        model.addAttribute("raw_account_balance", rawAccountBalance);
        model.addAttribute("address", address);
        model.addAttribute("city", city);
        model.addAttribute("state", state);
        model.addAttribute("zip", zip);
        model.addAttribute("country", country);
        model.addAttribute("phone", phone);
        model.addAttribute("term_code", termCode);
        model.addAttribute("sdkUrl", sdkUrl);
    }

    return "index";
}


    @PostMapping("/index")
    public String indexPost(
            @RequestParam(name = "inputAmt", required = false) Integer inputAmt,
            @RequestParam(name = "inputEmail", required = false) String inputEmail,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (inputAmt == null) {
            redirectAttributes.addFlashAttribute("error", "Amount is required.");
            return "redirect:/index";
        }

        if (authentication != null && authentication.getPrincipal() instanceof Saml2AuthenticatedPrincipal principal) {

            String student_id = principal.getFirstAttribute("student_id");
            String firstName = principal.getFirstAttribute("firstName");
            String lastName = principal.getFirstAttribute("lastName");
            String email =  principal.getFirstAttribute("email");
            String account_balance = principal.getFirstAttribute("account_balance");
            String address = principal.getFirstAttribute("address");
            String city = principal.getFirstAttribute("city");
            String state = principal.getFirstAttribute("state");
            String zip = principal.getFirstAttribute("zip");
            String country = principal.getFirstAttribute("country");
            String phone = principal.getFirstAttribute("phone");
            String termCode = principal.getFirstAttribute("term_code");

            flywireService.generateSession(
                    firstName, lastName, email, student_id,
                    account_balance, address, city, state, zip, country, phone, inputAmt, termCode, inputEmail
            );
        }

        // PRG Pattern: redirect after POST
        redirectAttributes.addFlashAttribute("success", "Payment session created.");
        return "redirect:/index";
    }
    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        model.addAttribute("reference", session.getAttribute("reference"));
        return "success";
    }
}
