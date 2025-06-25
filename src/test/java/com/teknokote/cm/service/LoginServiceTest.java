package com.teknokote.cm.service;

import com.teknokote.cm.authentification.model.LoginRequest;
import com.teknokote.cm.authentification.model.LoginResponse;
import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.model.Customer;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Test
    void testLoginRequestValidationUsernameIsNull() {
        // Arrange
        loginRequest = new LoginRequest(null, "password"); // Username is null

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            loginService.login(loginRequest, true);
        });

        assertEquals("username or password mismatch", exception.getMessage());
    }
    @Test
    void testLoginRequestValidationUserNotFound() {
        // Arrange
        loginRequest = new LoginRequest("nonexistentuser", "password");

        // Mock user retrieval to return empty
        when(userRepository.findAllByUsernameIgnoreCase("nonexistentuser")).thenReturn(Optional.empty());

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            loginService.login(loginRequest, true);
        });

        assertEquals("username or password mismatch", exception.getMessage());
    }
    @Test
    void testProcessCustomerRelationships_Success() {
        // Arrange
        String username = "testuser";
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("mockAccessToken");
        loginResponse.setCustomerId(1L); // Set a mock customer ID to test the response

        // Mock user retrieval
        User user = User.builder().username(username).build();
        when(userDao.getRepository().findAllByUsernameIgnoreCase(username)).thenReturn(Optional.of(user));

        // Create a mock Customer
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getId()).thenReturn(1L);
        when(mockCustomer.getSupplaierId()).thenReturn(2L); // Simulating a supplierId

        // Mock the CustomerDao to return the mock Customer
        when(customerDao.getRepository().findCustomerByUser(username)).thenReturn(Optional.of(mockCustomer));

        // Mock the response from RestTemplate to return a success LoginResponse
        ResponseEntity<LoginResponse> responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(eq(tokenEndpoint), any(HttpEntity.class), eq(LoginResponse.class)))
                .thenReturn(responseEntity);

        // Act
        LoginResponse result = loginService.login(new LoginRequest(username, "password"), true);

        // Assert
        assertNotNull(result);
        assertEquals("mockAccessToken", result.getAccessToken());
        assertEquals(1L, result.getCustomerId());
        assertEquals(2L, result.getSupplierId());

    }
    @Test
    void testLoginFailureDueToNullResponseBody() {
        // Arrange
        loginRequest = new LoginRequest("testuser", "password");
        when(userRepository.findAllByUsernameIgnoreCase("testuser"))
                .thenReturn(Optional.of(User.builder().username("testuser").build()));

        ResponseEntity<LoginResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK); // Simulate a null body
        when(restTemplate.postForEntity(eq(tokenEndpoint), any(HttpEntity.class), eq(LoginResponse.class)))
                .thenReturn(responseEntity);

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            loginService.login(loginRequest, true);
        });

        assertEquals("Invalid login response", exception.getMessage());
    }
}