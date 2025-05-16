package com.teknokote.cm.service;

import com.teknokote.cm.core.service.impl.AuthorizationServiceImpl;
import com.teknokote.cm.dto.AuthorizationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorizationServiceImplTest {

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;
    private AuthorizationDto authorizationDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorizationDto = AuthorizationDto.builder().id(1L).build();
    }

    @Test
    void evaluateCondition_TrueSingleCondition() {
        // Arrange
        String condition = "allowedProduct == 'productX'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateCondition_FalseSingleCondition() {
        // Arrange
        String condition = "allowedProduct == 'productX'";
        String productName = "productY";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
    @Test
    void evaluateCondition_TrueMultipleConditions_OR() {
        // Arrange
        String condition = "allowedProduct == 'productX' OR allowedProduct == 'productY'";
        String productName = "productY";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateCondition_FalseMultipleConditions_OR() {
        // Arrange
        String condition = "allowedProduct == 'productX' OR allowedProduct == 'productY'";
        String productName = "productZ";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
    @Test
    void evaluateCondition_TrueMultipleConditions_AND() {
        // Arrange
        String condition = "allowedProduct == 'productX' AND allowedSalePoints == 'sale_point_1'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateCondition_FalseMultipleConditions_AND() {
        // Arrange
        String condition = "allowedProduct == 'productX' AND allowedSalePoints == 'sale_point_1'";
        String productName = "productX";
        String salePointName = "sale_point_2";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
}