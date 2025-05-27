package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.service.impl.CustomerServiceImpl;
import com.teknokote.cm.core.service.impl.KeycloakService;
import com.teknokote.cm.core.service.interfaces.UserService;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.cm.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerDao customerDao;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void findCustomerByFilter_ShouldReturnEmptyList_WhenIdentifierIsNull() {
        // Act
        List<CustomerDto> result = customerService.findCustomerByFilter(null);

        // Assert
        assertTrue(result.isEmpty());
        verify(customerDao, never()).findCustomerByIdentifier(any());
    }
    @Test
    void findCustomerByFilter_ShouldReturnCustomers_WhenIdentifierIsProvided() {
        // Arrange
        String identifier = "customer1";
        List<CustomerDto> expectedCustomers = List.of(CustomerDto.builder().build());
        when(customerDao.findCustomerByIdentifier(identifier)).thenReturn(expectedCustomers);

        // Act
        List<CustomerDto> result = customerService.findCustomerByFilter(identifier);

        // Assert
        assertEquals(expectedCustomers, result);
        verify(customerDao, times(1)).findCustomerByIdentifier(identifier);
    }
    @Test
    void findCustomerBySupplier_ShouldReturnEmptyList_WhenSupplierIdIsNull() {
        // Act
        List<CustomerDto> result = customerService.findCustomerBySupplier(null);

        // Assert
        assertTrue(result.isEmpty());
        verify(customerDao, never()).findCustomerBySupplier(any());
    }
    @Test
    void findCustomerBySupplier_ShouldReturnCustomers_WhenSupplierIdIsProvided() {
        // Arrange
        Long supplierId = 1L;
        List<CustomerDto> expectedCustomers = List.of(CustomerDto.builder().build());
        when(customerDao.findCustomerBySupplier(supplierId)).thenReturn(expectedCustomers);

        // Act
        List<CustomerDto> result = customerService.findCustomerBySupplier(supplierId);

        // Assert
        assertEquals(expectedCustomers, result);
        verify(customerDao, times(1)).findCustomerBySupplier(supplierId);
    }
    @Test
    void findByIdentifier_ShouldReturnEmpty_WhenIdentifierIsBlank() {
        // Act
        Optional<CustomerDto> result = customerService.findByIdentifier(" ");

        // Assert
        assertTrue(result.isEmpty());
        verify(customerDao, never()).findByIdentifier(any());
    }
    @Test
    void findByIdentifier_ShouldReturnCustomer_WhenIdentifierIsProvided() {
        // Arrange
        String identifier = "customer1";
        CustomerDto expectedCustomer = CustomerDto.builder().build();
        when(customerDao.findByIdentifier(identifier)).thenReturn(Optional.of(expectedCustomer));

        // Act
        Optional<CustomerDto> result = customerService.findByIdentifier(identifier);

        // Assert
        assertEquals(Optional.of(expectedCustomer), result);
        verify(customerDao, times(1)).findByIdentifier(identifier);
    }
    @Test
    void addCustomer_ShouldCallKeycloakService_WhenUsersAreNotNullOrEmpty() {
        // Arrange
        CustomerDto customerDto = CustomerDto.builder().build();
        customerDto.setUsers(new HashSet<>(Set.of(UserDto.builder().build()))); // Adding a user

        when(customerDao.create(eq(customerDto))).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.addCustomer(customerDto);

        // Assert
        assertEquals(customerDto, result);
        verify(keycloakService, times(1)).createUser(any(UserDto.class));
    }
    @Test
    void addCustomer_ShouldNotCallKeycloakService_WhenUsersAreEmpty() {
        // Arrange
        CustomerDto customerDto = CustomerDto.builder().build();
        customerDto.setUsers(Collections.emptySet());

        when(customerDao.create(any())).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.addCustomer(customerDto);

        // Assert
        assertEquals(customerDto, result);
        verify(keycloakService, never()).createUser(any());
    }
    @Test
    void update_ShouldUpdateUserInKeycloak_WhenUserIsPresent() {
        // Arrange
        CustomerDto customerDto = CustomerDto.builder().build();
        customerDto.setIdentifier("test_user");
        customerDto.setUsers(new HashSet<>(Set.of(UserDto.builder().build()))); // User that will be updated

        when(customerDao.update(any(CustomerDto.class))).thenReturn(customerDto);
        when(userService.findByUsername("test_user")).thenReturn(Optional.of(UserDto.builder().build()));

        // Act
        CustomerDto result = customerService.update(customerDto);

        // Assert
        assertEquals(customerDto, result);
        verify(keycloakService, times(1)).updateUser(eq(customerDto.getIdentifier()), any(UserDto.class));
    }
    @Test
    void update_ShouldNotUpdateUserInKeycloak_WhenUserNotFound() {
        // Arrange
        CustomerDto customerDto = CustomerDto.builder().build();
        customerDto.setIdentifier("unknown_user");
        customerDto.setUsers(new HashSet<>(Set.of(UserDto.builder().build())));

        when(customerDao.update(any(CustomerDto.class))).thenReturn(customerDto);
        when(userService.findByUsername("unknown_user")).thenReturn(Optional.empty());

        // Act
        CustomerDto result = customerService.update(customerDto);

        // Assert
        assertEquals(customerDto, result);
        verify(keycloakService, never()).updateUser(any(), any());
    }
    @Test
    void generateIdentifierSuggestions_ShouldReturnEmptyList_WhenIdentifierIsNull() {
        // Act
        List<String> result = customerService.generateIdentiferSuggestions(null);

        // Assert
        assertTrue(result.isEmpty());
        verify(customerDao, never()).generateIdentiferSuggestions(any());
    }
    @Test
    void generateIdentifierSuggestions_ShouldReturnEmptyList_WhenIdentifierIsEmpty() {
        // Act
        List<String> result = customerService.generateIdentiferSuggestions("");

        // Assert
        assertTrue(result.isEmpty());
        verify(customerDao, never()).generateIdentiferSuggestions(any());
    }
    @Test
    void generateIdentifierSuggestions_ShouldReturnSuggestions_WhenIdentifierIsProvided() {
        // Arrange
        String identifier = "cust";
        List<String> expectedSuggestions = List.of("cust1", "cust2");
        when(customerDao.generateIdentiferSuggestions(identifier)).thenReturn(expectedSuggestions);

        // Act
        List<String> result = customerService.generateIdentiferSuggestions(identifier);

        // Assert
        assertEquals(expectedSuggestions, result);
        verify(customerDao, times(1)).generateIdentiferSuggestions(identifier);
    }
}