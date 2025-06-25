package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.ProductDao;
import com.teknokote.cm.core.dao.SalePointDao;
import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.service.impl.SupplierServiceImpl;
import com.teknokote.cm.dto.ProductDto;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ESSValidationResult;
import com.teknokote.core.service.ESSValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SupplierServiceImplTest {

    @InjectMocks
    private SupplierServiceImpl supplierService;
    @Mock
    private SupplierDao supplierDao;
    @Mock
    private SalePointDao salePointDao;
    @Mock
    private UserDao userDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private ESSValidator<SupplierDto> validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testCreateSupplier_Success() {
        // Arrange
        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setReference("REF123");
        supplierDto.setIdentifier("ID123");

        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();

        when(validator.validateOnCreate(supplierDto)).thenReturn(validationResult);

        when(supplierDao.findAllByReferenceAndIdentifier(supplierDto.getReference(), supplierDto.getIdentifier())).thenReturn(null);
        when(supplierDao.create(any(SupplierDto.class))).thenReturn(supplierDto);

        // Act
        SupplierDto result = supplierService.createSupplier(supplierDto);

        // Assert
        assertNotNull(result);
        assertEquals(supplierDto, result);
        verify(supplierDao).create(supplierDto);
    }
    @Test
    void testCreateSupplier_Duplicate() {
        // Arrange
        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setReference("REF123");
        supplierDto.setIdentifier("ID123");

        when(supplierDao.findAllByReferenceAndIdentifier(supplierDto.getReference(), supplierDto.getIdentifier())).thenReturn(supplierDto);

        // Act & Assert
        ServiceValidationException thrown = assertThrows(ServiceValidationException.class, () -> {
            supplierService.createSupplier(supplierDto);
        });
        assertEquals("supplier already exported", thrown.getMessage());
    }
    @Test
    void testUpdateSupplier_Success() {
        // Arrange
        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setReference("REF123");
        supplierDto.setIdentifier("ID123");

        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();

        when(validator.validateOnUpdate(supplierDto)).thenReturn(validationResult);

        when(supplierDao.findAllByReferenceAndIdentifier(supplierDto.getReference(), supplierDto.getIdentifier())).thenReturn(supplierDto);
        when(supplierDao.update(any(SupplierDto.class))).thenReturn(supplierDto);

        // Act
        SupplierDto result = supplierService.updateSupplier(supplierDto);

        // Assert
        assertNotNull(result);
        assertEquals(supplierDto, result);
        verify(supplierDao).update(supplierDto);
    }
    @Test
    void testCreateSalePoint_Success() {
        // Arrange
        SalePointDto salePointDto = SalePointDto.builder().build();
        salePointDto.setIdentifier("SP123");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(1L);

        when(supplierService.findById(supplierDto.getId())).thenReturn(Optional.of(supplierDto));
        when(salePointDao.create(salePointDto)).thenReturn(salePointDto);

        // Act
        SalePointDto result = supplierService.createSalePoint(supplierDto.getId(), salePointDto);

        // Assert
        assertNotNull(result);
        assertEquals(salePointDto, result);
    }
    @Test
    void testAddProduct_Success() {
        // Arrange
        ProductDto productDto = ProductDto.builder().build();
        productDto.setReference("REF123");
        productDto.setName("Product A");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(1L);

        when(supplierService.findByReference(productDto.getReference())).thenReturn(supplierDto);
        when(productDao.findProductWithName(productDto.getName(), supplierDto.getId())).thenReturn(null);
        when(productDao.create(productDto)).thenReturn(productDto);

        // Act
        ProductDto result = supplierService.addProduct(productDto);

        // Assert
        assertNotNull(result);
        assertEquals(productDto, result);
    }
    @Test
    void testAddProduct_DuplicateProduct() {
        // Arrange
        ProductDto productDto = ProductDto.builder().build();
        productDto.setReference("REF123");
        productDto.setName("Product A");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(1L);

        ProductDto existingProduct = ProductDto.builder().build();
        existingProduct.setId(2L);
        existingProduct.setName("Product A");

        // Mocking the behaviors
        when(supplierService.findByReference(productDto.getReference())).thenReturn(supplierDto);
        when(productDao.findProductWithName(productDto.getName(), supplierDto.getId())).thenReturn(existingProduct);
        when(productDao.update(productDto)).thenReturn(productDto);

        // Act
        ProductDto result = supplierService.addProduct(productDto);

        // Assert
        assertNotNull(result);
        assertEquals(existingProduct.getId(), result.getId());
        verify(productDao).update(productDto);
    }
    @Test
    void testUpdateSalePoint_Success() {
        // Arrange
        Long supplierId = 1L;
        SalePointDto salePointDto = SalePointDto.builder().build();
        salePointDto.setIdentifier("SP123");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(supplierId);
        SalePointDto existingSalePoint = SalePointDto.builder().build();
        existingSalePoint.setId(2L);
        existingSalePoint.setIdentifier("SP123");
        supplierDto.setSalePoints(Collections.singleton(existingSalePoint));

        // Mocking findById and update behavior
        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplierDto));
        when(salePointDao.update(any(SalePointDto.class))).thenReturn(salePointDto);

        // Act
        SalePointDto result = supplierService.updateSalePoint(supplierId, salePointDto);

        // Assert
        assertNotNull(result);
        assertEquals(salePointDto, result);
        verify(salePointDao).update(salePointDto);
    }
    @Test
    void testUpdateSalePoint_NotFound() {
        // Arrange
        Long supplierId = 1L;
        SalePointDto salePointDto = SalePointDto.builder().build();
        salePointDto.setIdentifier("SP123");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(supplierId);
        supplierDto.setSalePoints(Collections.emptySet());  // No sale points

        // Mocking findById behavior
        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplierDto));

        // Act
        SalePointDto result = supplierService.updateSalePoint(supplierId, salePointDto);

        // Assert
        assertNull(result);
    }
    @Test
    void testCreateUser_Success() {
        // Arrange
        Long supplierId = 1L;
        UserDto userDto = UserDto.builder().build();
        userDto.setUserIdentifier("USER123");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(supplierId);
        supplierDto.setUsers(new HashSet<>());

        // Mocking findById behavior
        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplierDto));
        when(supplierDao.update(supplierDto)).thenReturn(supplierDto);

        // Act
        UserDto result = supplierService.createUser(supplierId, userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto, result);
        assertTrue(supplierDto.getUsers().contains(userDto));
        verify(supplierDao).update(supplierDto);
    }
    @Test
    void testUpdateUser_Success() {
        // Arrange
        Long supplierId = 1L;
        UserDto userDto = UserDto.builder().build();
        userDto.setUserIdentifier("USER123");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(supplierId);
        UserDto existingUser = UserDto.builder().build();
        existingUser.setId(2L);
        existingUser.setUserIdentifier("USER123");
        supplierDto.setUsers(new HashSet<>(Arrays.asList(existingUser)));

        // Mocking findById behavior and update behavior
        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplierDto));
        when(userDao.update(any(UserDto.class))).thenReturn(userDto);

        // Act
        UserDto result = supplierService.updateUser(supplierId, userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto, result);
        verify(userDao).update(userDto);
    }
    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        Long supplierId = 1L;
        UserDto userDto = UserDto.builder().build();
        userDto.setUserIdentifier("USER123");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(supplierId);
        supplierDto.setUsers(new HashSet<>(Arrays.asList())); // Empty user set

        // Mocking findById behavior
        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplierDto));

        // Act
        UserDto result = supplierService.updateUser(supplierId, userDto);

        // Assert
        assertEquals(userDto, result);
    }
    @Test
    void testAddProduct_NonExistentSupplier() {
        // Arrange
        ProductDto productDto = ProductDto.builder().build();
        productDto.setReference("REF123");

        // Mocking the behavior that the supplier does not exist
        when(supplierService.findByReference(productDto.getReference())).thenReturn(null);

        // Act & Assert
        ServiceValidationException thrown = assertThrows(ServiceValidationException.class, () -> {
            supplierService.addProduct(productDto);
        });
        assertEquals("supplier not existing on card manager !!!", thrown.getMessage());
    }
    @Test
    void testCreateUser_CleanupDeletedUsers() {
        // Arrange
        Long supplierId = 1L;
        UserDto userDto = UserDto.builder().build();
        userDto.setId(3L); // New user
        userDto.setUserIdentifier("USER123");

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(supplierId);
        UserDto existingUser1 = UserDto.builder().build();
        existingUser1.setId(1L);
        existingUser1.setUserIdentifier("USER111"); // Existing but not being removed

        UserDto existingUser2 = UserDto.builder().build();
        existingUser2.setId(2L);
        existingUser2.setUserIdentifier("USER222"); // Existing and should be removed

        supplierDto.setUsers(new HashSet<>(Arrays.asList(existingUser1, existingUser2)));
        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplierDto));
        when(supplierDao.update(any(SupplierDto.class))).thenReturn(supplierDto);

        // Act
        UserDto result = supplierService.createUser(supplierId, userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto, result);
        assertTrue(supplierDto.getUsers().contains(userDto)); // Assert the new user was added
    }
    @Test
    void testCreateUser_UserCleanup() {
        // Arrange
        Long supplierId = 1L;
        UserDto userDto = UserDto.builder().build();
        userDto.setId(3L); // New user

        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(supplierId);
        UserDto existingUser1 = UserDto.builder().build();
        existingUser1.setId(1L);
        existingUser1.setUserIdentifier("USER111"); // Existing user
        UserDto existingUser2 = UserDto.builder().build();
        existingUser2.setId(2L);
        existingUser2.setUserIdentifier("USER222"); // Existing user to be removed

        // Set up the supplier to have existing users
        supplierDto.setUsers(new HashSet<>(Arrays.asList(existingUser1, existingUser2)));
        when(supplierService.findById(supplierId)).thenReturn(Optional.of(supplierDto));
        when(supplierDao.update(any(SupplierDto.class))).thenReturn(supplierDto); // Return updated supplier

        // Act
        UserDto result = supplierService.createUser(supplierId, userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto, result);
        assertTrue(supplierDto.getUsers().contains(userDto)); // Check the new user was added
        assertTrue(supplierDto.getUsers().contains(existingUser2)); // Existing user2 should be removed
    }
}