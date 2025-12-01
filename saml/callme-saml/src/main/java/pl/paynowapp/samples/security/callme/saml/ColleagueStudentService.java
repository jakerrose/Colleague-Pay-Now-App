package pl.paynowapp.samples.security.callme.saml;

import jakarta.annotation.PostConstruct;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.paynowapp.samples.security.callme.saml.address.Address;
import pl.paynowapp.samples.security.callme.saml.person.Email;
import pl.paynowapp.samples.security.callme.saml.person.Name;
import pl.paynowapp.samples.security.callme.saml.person.Person;
import pl.paynowapp.samples.security.callme.saml.accountActivity.AccountActivity;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.paynowapp.samples.security.callme.saml.person.Phone;

@Service
public class ColleagueStudentService {

    private final RestTemplate restTemplate;
    private final ColleagueProperties colleagueProperties;
    @Value("${ELLUCIAN_BEARER_TOKEN}")
    private String bearerToken;// Replace or inject securely
    HttpHeaders headers = new HttpHeaders();
    private static final Logger logger = LoggerFactory.getLogger(ColleagueStudentService.class);

    public ColleagueStudentService(RestTemplate restTemplate, ColleagueProperties colleagueProperties) {
        this.restTemplate = restTemplate;
        this.colleagueProperties = colleagueProperties;
    }

    @PostConstruct
    private void initHeaders() {
        // This will run after @Value fields are injected
        String credentials = bearerToken;
        //String encoded = Base64.getEncoder().encodeToString(bearerToken.getBytes());
        headers.add("Authorization", "Basic " + bearerToken);
        headers.add("Accept", "application/json");
        headers.add("X-Media-Type", "application/vnd.hedtech.integration.v12+json");
    }

    public String getStudentData() {
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://dc.train.ellucian.com:8060/ColleagueApi",
                HttpMethod.GET, // or POST if needed
                request,
                String.class
        );

        return response.getBody();
    }

    public Person getStudentByColleagueId(String studentId) {

        String criteria = "{\"credentials\":[{\"type\":\"colleaguePersonId\",\"value\":\"" + studentId + "\"}]}";
        String encodedCriteria = URLEncoder.encode(criteria, StandardCharsets.UTF_8);

        URI uri = URI.create("http://dc.train.ellucian.com:8060/ColleagueApi/persons?criteria=" + encodedCriteria);
        logger.debug("Final url: {}", uri);

        HttpHeaders headers = new HttpHeaders();
        //String encoded = Base64.getEncoder().encodeToString(bearerToken.getBytes());
        headers.set("Authorization", "Basic " + bearerToken);
        //headers.set("Accept", "application/json");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("X-COM-PERSIST", "NO");
        headers.set("X-Media-Type", "application/vnd.hedtech.integration.v12+json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        logger.info(">>> Headers: {}", headers);

        try {
            ResponseEntity<Person[]> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Person[].class
            );

            logger.debug("Response status: {}", response.getStatusCode());
            logger.debug("Response body: {}", Arrays.toString(response.getBody()));

            Person[] body = response.getBody();
            return (body != null && body.length > 0) ? body[0] : null;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("API error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while calling Colleague API", ex);
            throw ex;
        }
    }

    public Address getAddressByAddressId(String addressId) {
        URI uri = URI.create("http://dc.train.ellucian.com:8060/ColleagueApi/addresses/" + addressId);
        logger.debug("Final url: {}", uri);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + bearerToken);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        logger.info(">>> Headers: {}", headers);

        try {
            ResponseEntity<Address> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Address.class
            );
            logger.debug("Response status: {}", response.getStatusCode());
            logger.debug("Response body: {}", response.getBody().toString());

            Address body = response.getBody();
            return body;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("API error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while calling Colleague API", ex);
            throw ex;
        }
    }

    public AccountActivity getBalancebyStudentId(String studentId) {
        URI uri = URI.create("http://dc.train.ellucian.com:8060/ColleagueApi/account-activity/admin/" + studentId);
        logger.debug("Final url: {}", uri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + bearerToken);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        logger.info(">>> Headers: {}", headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            logger.debug("Response status: {}", response.getStatusCode());
            logger.debug("Raw JSON response body: {}", response.getBody());

            String body = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            AccountActivity accountActivity = objectMapper.readValue(body, AccountActivity.class);

            return accountActivity;
        } catch (Exception e) {
            logger.error("Error fetching account activity for studentId {}: {}", studentId, e.getMessage());
            return null;
        }
    }

    public Optional<Address> getConfiguredAddress(Person person) {
        String configuredType = colleagueProperties.getAddressType().toLowerCase();

        // Step 1: pick addressId from student's addresses
        Optional<String> addressIdOpt = person.getAddresses().stream()
                .filter(addrWrapper -> addrWrapper.getType() != null
                        && addrWrapper.getType().getAddressType() != null
                        && addrWrapper.getType().getAddressType().equalsIgnoreCase(configuredType))
                .map(addrWrapper -> addrWrapper.getAddress().getId())
                .findFirst();

        // Step 2: if found, fetch it via existing API call
        return addressIdOpt.map(this::getAddressByAddressId);
    }

    public Optional<Email> getConfiguredEmail(Person person) {
        String configuredType = colleagueProperties.getEmailType().toLowerCase();

        return person.getEmails().stream()
                .filter(email -> email.getType() != null
                        && email.getType().getEmailType() != null
                        && email.getType().getEmailType().equalsIgnoreCase(configuredType))
                .findFirst();
    }

    public Optional<Phone> getConfiguredPhone(Person person) {
        String configuredType = colleagueProperties.getPhoneType().toLowerCase();

        return person.getPhones().stream()
                .filter(phone -> phone.getType() != null
                        && phone.getType().getPhoneType() != null
                        && phone.getType().getPhoneType().equalsIgnoreCase(configuredType))
                .findFirst();
    }

    public Optional<Name> getConfiguredName(Person person) {
        String configuredType = colleagueProperties.getNameType().toLowerCase();

        return person.getNames().stream()
                .filter(name -> name.getType() != null
                        && name.getType().getCategory() != null
                        && name.getType().getCategory().equalsIgnoreCase(configuredType))
                .findFirst();
    }
}

