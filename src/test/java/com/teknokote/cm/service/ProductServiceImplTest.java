package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.ProductDao;
import com.teknokote.cm.core.service.impl.ProductServiceImpl;
import com.teknokote.cm.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBySupplier() {
        // Arrange
        Long supplierId = 1L;
        ProductDto product1 = ProductDto.builder().name("Product 1").build();
        ProductDto product2 = ProductDto.builder().name("Product 2").build();

        List<ProductDto> expectedProducts = List.of(product1, product2);

        when(productDao.findBySupplier(supplierId)).thenReturn(expectedProducts);

        // Act
        List<ProductDto> actualProducts = productService.findBySupplier(supplierId);

        // Assert
        assertEquals(expectedProducts, actualProducts);
    }
    @Test
    void testFindBySupplierAndName() {
        // Arrange
        Long supplierId = 1L;
        String productName = "Product A";
        ProductDto expectedProduct = ProductDto.builder().name(productName).build();

        when(productDao.findProductWithName(productName, supplierId)).thenReturn(expectedProduct);

        // Act
        ProductDto actualProduct = productService.findBySupplierAndName(productName, supplierId);

        // Assert
        assertEquals(expectedProduct, actualProduct);
    }
}