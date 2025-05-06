package com.teknokote.cm.service;

import com.teknokote.cm.authentification.model.LoginRequest;
import com.teknokote.cm.authentification.model.LoginResponse;
import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.CustomerRepository;
import com.teknokote.cm.core.repository.SupplierRepository;
import com.teknokote.cm.core.repository.UserRepository;
import com.teknokote.cm.core.service.authentication.LoginService;
import com.teknokote.cm.core.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserDao userDao;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private UserService userService;
    @Mock
    private SupplierDao supplierDao;
    @Mock
    private CustomerDao customerDao;
    @Mock
    private CustomerRepository customerRepository;

    @Value("${spring.security.oauth2.client.provider.keycloak.realm}")
    public String realm;
    @Value("${spring.security.oauth2.client.provider.keycloak.token_endpoint}")
    private String tokenEndpoint;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loginRequest = new LoginRequest("testuser", "password");

        when(userDao.getRepository()).thenReturn(userRepository);
        when(supplierDao.getRepository()).thenReturn(supplierRepository);
        when(customerDao.getRepository()).thenReturn(customerRepository);

        loginService = new LoginService(restTemplate,userDao,userService, supplierDao, customerDao);
    }
    @Test
    void login_Success() {
        // Arrange
        loginRequest = new LoginRequest("testuser", "password");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("mockAccessToken");

        // Mock user retrieval
        when(userRepository.findAllByUsernameIgnoreCase("testuser"))
                .thenReturn(Optional.of(User.builder().username("testuser").build()));

        // Mock response from REST call
        ResponseEntity<LoginResponse> responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);

        // Mocking RestTemplate
        when(restTemplate.postForEntity(eq(tokenEndpoint), any(HttpEntity.class), eq(LoginResponse.class)))
                .thenReturn(responseEntity);

        // Act
        LoginResponse result = loginService.login(loginRequest, true);

        // Assert results
        assertNotNull(result);
        assertEquals("mockAccessToken", result.getAccessToken());
    }
}