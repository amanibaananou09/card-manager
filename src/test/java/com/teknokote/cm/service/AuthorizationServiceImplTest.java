package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.model.EnumCeilingType;
import com.teknokote.cm.core.service.impl.AuthorizationServiceImpl;
import com.teknokote.cm.core.service.interfaces.TransactionService;
import com.teknokote.cm.dto.*;
import com.teknokote.core.service.ESSValidationResult;
import com.teknokote.core.service.ESSValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorizationServiceImplTest {

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;
    @Mock
    private AuthorizationDao authorizationDao;
    @Mock
    private TransactionService transactionService;
    @Mock
    private ESSValidator<AuthorizationDto> validator;

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
    @Test
    void evaluateOrPart_False() {
        // Arrange
        String orCondition = "allowedProduct == 'productX' AND allowedSalePoints == 'sale_point_2' OR allowedProduct == 'productY'";
        String productName = "productZ";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateOrPart(orCondition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedDays() {
        // Arrange
        String andPart = "allowedDays == 'MONDAY'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedSalePoints() {
        // Arrange
        String andPart = "allowedSalePoints == 'sale_point_1'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedCity() {
        // Arrange
        String andPart = "allowedCity == 'cityX'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedProduct() {
        // Arrange
        String andPart = "allowedProduct == 'productX'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidNotCondition() {
        // Arrange
        String andPart = "allowedProduct == 'productY'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart("allowedProduct == 'productY'", productName, salePointName, day, city);

        // Assert false because the product is not allowed
        assertFalse(result);
    }
    @Test
    void evaluateAndPart_NegatedCondition() {
        // Arrange
        String andPart = "allowedProduct == 'productY'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart("allowedProduct == 'productY' not", productName, salePointName, day, city);

        // Assert true because the product is not allowed, and we negated the result
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_MalformedExpression() {
        // Arrange
        String andPart = "allowedDays MONDAY";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);
        });
        assertEquals("Malformed expression: " + andPart, thrown.getMessage());
    }
    @Test
    void evaluateAndPart_UnrecognizedProperty() {
        // Arrange
        String andPart = "unknownProperty == 'value'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "cityX";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);
        });
        assertEquals("Unrecognized property: unknownProperty", thrown.getMessage());
    }
    @Test
    void evaluateAndPart_SpecialHandlingForNull() {
        // Arrange
        String andPart = "allowedCity == 'null'";
        String productName = "productX";
        String salePointName = "sale_point_1";
        String day = "MONDAY";
        String city = "anyCity";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateTimeSlot_MalformedSlot_ThrowsException() {
        // Arrange
        String malformedSlot = "10:00 to or 12:00";

        // Act & Assert
        assertThrows(DateTimeParseException.class, () -> {
            authorizationService.evaluateTimeSlot(malformedSlot);
        });
    }
    @Test
    void evaluateTimeSlot_MalformedTimeSlot_ThrowsException() {
        // Arrange
        String malformedSlot = "10:00 to or 12:00";

        // Act & Assert
        Exception exception = assertThrows(DateTimeParseException.class, () -> {
            authorizationService.evaluateTimeSlot(malformedSlot);
        });
        assertEquals("Text '' could not be parsed at index 0", exception.getMessage());
    }
    @Test
    void findByPtsIdAndPump_ValidCase() {
        // Arrange
        String ptsId = "somePtsId";
        Long pump = 1L;
        String tag = "someTag";
        AuthorizationDto expectedDto = AuthorizationDto.builder().build();
        when(authorizationService.getDao().findAuthorizationByPtsIdAndPump(ptsId, pump, tag)).thenReturn(expectedDto);

        // Act
        authorizationDto = authorizationService.findByPtsIdAndPump(ptsId, pump, tag);

        // Assert
        assertNotNull(authorizationDto);
        assertEquals(expectedDto, authorizationDto);
    }
    @Test
    void authorizeIfAuthorized_LastTransactionPresent_BelowLimit() {
        // Arrange
        CardDto cardDto = CardDto.builder().id(1L).build();
        CeilingDto ceilingDto = CeilingDto.builder().build();

        // Set ceiling type to a valid enum value
        ceilingDto.setCeilingType(EnumCeilingType.AMOUNT);
        BigDecimal limit = BigDecimal.valueOf(100);

        AuthorizationRequest authorizationRequest = AuthorizationRequest.builder().build();

        // Mock the last transaction to be present and below the limit
        TransactionDto lastTransaction = TransactionDto.builder().build();
        lastTransaction.setAmount(BigDecimal.valueOf(50));

        ESSValidationResult validationResultMock = mock(ESSValidationResult.class);
        when(validator.validateOnCreate(any())).thenReturn(validationResultMock);

        when(transactionService.findLastTransactionByCardId(any(), any(), any())).thenReturn(Optional.of(lastTransaction));

        // Act
        authorizationService.authorizeIfAuthorized(cardDto, ceilingDto, "generatedRef", limit, authorizationRequest);

    }
    @Test
    void authorizeIfAuthorized_LastTransactionPresent_AboveLimit() {
    }
}