package com.teknokote.cm.core.service.authentication;

import com.teknokote.cm.authentification.model.LoginRequest;
import com.teknokote.cm.authentification.model.LoginResponse;
import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.model.Customer;
import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.service.UserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
@Getter
@Setter
@Slf4j
public class LoginService {
    @Value("${spring.security.oauth2.client.provider.keycloak.realm}")
    public String realm;
    @Value("${spring.security.oauth2.client.provider.keycloak.serverUrl}")
    private String serverUrl;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.provider.keycloak.token_endpoint}")
    private String tokenEndpoint;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private UserService userService;
    private final RestTemplate restTemplate;

    @Autowired
    public LoginService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse login(LoginRequest loginrequest, boolean doCheckUser) {
        if (doCheckUser) {
            if (Objects.isNull(loginrequest.getUsername())) {
                throw new BadCredentialsException("username or password mismatch");
            }
            final Optional<User> user = getUserDao().getRepository().findAllByUsernameIgnoreCase(loginrequest.getUsername());
            if (user.isEmpty()) {
                throw new BadCredentialsException("username or password mismatch");
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
        map.add("username", loginrequest.getUsername());
        map.add("password", loginrequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        log.info("Trying to log in: {}", loginrequest.getUsername());
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(tokenEndpoint, httpEntity, LoginResponse.class);

        // Check if the response is successful and has a body
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            userService.updateLastConnection(loginrequest.getUsername());
            log.info("{} -> logged in", loginrequest.getUsername());

            final Optional<User> optionalUser = getUserDao().getRepository().findAllByUsernameIgnoreCase(loginrequest.getUsername());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Optional<Supplier> optionalSupplier = getSupplierDao().getRepository().findSupplierByUser(user.getUsername());
                if (optionalSupplier.isPresent()) {
                    response.getBody().setSupplierId(optionalSupplier.get().getId());
                }
                Optional<Customer> optionalCustomer = getCustomerDao().getRepository().findCustomerByUser(user.getUsername());
                if (optionalCustomer.isPresent()) {
                    response.getBody().setCustomerId(optionalCustomer.get().getId());
                    response.getBody().setSupplierId(optionalCustomer.get().getSupplaierId());
                }
            }
            return response.getBody();
        } else {
            log.info("{} -> Failed to log in, result: {}", loginrequest.getUsername(), response.getBody());
            throw new BadCredentialsException("Invalid login response");
        }
    }
}
