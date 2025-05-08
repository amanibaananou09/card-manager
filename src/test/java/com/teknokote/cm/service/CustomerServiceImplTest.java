package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.service.impl.CustomerServiceImpl;
import com.teknokote.cm.core.service.impl.KeycloakService;
import com.teknokote.cm.core.service.interfaces.UserService;
import com.teknokote.cm.dto.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
}