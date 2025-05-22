package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.model.EnumAuthorizationStatus;
import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.model.EnumCeilingType;
import com.teknokote.cm.core.service.impl.AuthorizationServiceImpl;
import com.teknokote.cm.core.service.interfaces.CardGroupService;
import com.teknokote.cm.core.service.interfaces.CardService;
import com.teknokote.cm.core.service.interfaces.SupplierService;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorizationServiceImplTest {

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;
    @Mock
    private AuthorizationDao authorizationDao;
    @Mock
    private CardService cardService;
    @Mock
    private SupplierService supplierService;
    @Mock
    private CardGroupService cardGroupService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private ESSValidator<AuthorizationDto> validator;
    private AuthorizationRequest authorizationRequest;
    private AuthorizationDto authorizationDto;
    private CardGroupDto cardGroupDto;
    private SalePointDto salePointDto;
    private SupplierDto supplierDto;
    private CeilingDto ceilingDto;
    private String salePointName;
    private String productName;
    private String condition;
    private CardDto cardDto;
    private String andPart;
    private String day;
    private String city;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorizationDto = AuthorizationDto.builder().id(1L).build();
    }

    @Test
    void evaluateCondition_TrueSingleCondition() {
        // Arrange
        condition = "allowedProduct == 'productX'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateCondition_FalseSingleCondition() {
        // Arrange
        condition = "allowedProduct == 'productX'";
        productName = "productY";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
    @Test
    void evaluateCondition_TrueMultipleConditions_OR() {
        // Arrange
        condition = "allowedProduct == 'productX' OR allowedProduct == 'productY'";
        productName = "productY";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateCondition_FalseMultipleConditions_OR() {
        // Arrange
        condition = "allowedProduct == 'productX' OR allowedProduct == 'productY'";
        productName = "productZ";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
    @Test
    void evaluateCondition_TrueMultipleConditions_AND() {
        // Arrange
        condition = "allowedProduct == 'productX' AND allowedSalePoints == 'sale_point_1'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateCondition_FalseMultipleConditions_AND() {
        // Arrange
        condition = "allowedProduct == 'productX' AND allowedSalePoints == 'sale_point_1'";
        productName = "productX";
        salePointName = "sale_point_2";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateCondition(condition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
    @Test
    void evaluateOrPart_False() {
        // Arrange
        String orCondition = "allowedProduct == 'productX' AND allowedSalePoints == 'sale_point_2' OR allowedProduct == 'productY'";
        productName = "productZ";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateOrPart(orCondition, productName, salePointName, day, city);

        // Assert
        assertFalse(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedDays() {
        // Arrange
        andPart = "allowedDays == 'MONDAY'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedSalePoints() {
        // Arrange
        andPart = "allowedSalePoints == 'sale_point_1'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedCity() {
        // Arrange
        andPart = "allowedCity == 'cityX'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidAllowedProduct() {
        // Arrange
        andPart = "allowedProduct == 'productX'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);

        // Assert
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_ValidNotCondition() {
        // Arrange
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart("allowedProduct == 'productY'", productName, salePointName, day, city);

        // Assert false because the product is not allowed
        assertFalse(result);
    }
    @Test
    void evaluateAndPart_NegatedCondition() {
        // Arrange
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act
        boolean result = authorizationService.evaluateAndPart("allowedProduct == 'productY' not", productName, salePointName, day, city);

        // Assert true because the product is not allowed, and we negated the result
        assertTrue(result);
    }
    @Test
    void evaluateAndPart_MalformedExpression() {
        // Arrange
        andPart = "allowedDays MONDAY";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);
        });
        assertEquals("Malformed expression: " + andPart, thrown.getMessage());
    }
    @Test
    void evaluateAndPart_UnrecognizedProperty() {
        // Arrange
        andPart = "unknownProperty == 'value'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "cityX";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.evaluateAndPart(andPart, productName, salePointName, day, city);
        });
        assertEquals("Unrecognized property: unknownProperty", thrown.getMessage());
    }
    @Test
    void evaluateAndPart_SpecialHandlingForNull() {
        // Arrange
        andPart = "allowedCity == 'null'";
        productName = "productX";
        salePointName = "sale_point_1";
        day = "MONDAY";
        city = "anyCity";

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
        cardDto = CardDto.builder().id(1L).build();
        ceilingDto = CeilingDto.builder().build();

        // Set ceiling type to a valid enum value
        ceilingDto.setCeilingType(EnumCeilingType.AMOUNT);
        BigDecimal limit = BigDecimal.valueOf(100);

        authorizationRequest = AuthorizationRequest.builder().build();

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
        // Arrange
        cardDto = createActiveCard();
        ceilingDto = CeilingDto.builder().ceilingType(EnumCeilingType.AMOUNT).value(BigDecimal.valueOf(100)).build();

        authorizationRequest = AuthorizationRequest.builder()
                .reference("A1")
                .salePointIdentifier("sale_point_1")
                .tag("card_tag")
                .productName("productX")
                .ptsId("testPtsId")
                .pump(1L)
                .build();

        // Mock the last transaction to be present and above the limit
        TransactionDto lastTransaction = TransactionDto.builder().availableBalance(BigDecimal.valueOf(150)).build();

        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);
        when(transactionService.findLastTransactionByCardId(any(), any(), any())).thenReturn(Optional.of(lastTransaction));

        when(authorizationDao.create(any())).thenAnswer(invocation -> {
            AuthorizationDto createdDto = AuthorizationDto.builder()
                    .id(1L)
                    .status(EnumAuthorizationStatus.GRANTED)
                    .build();
            return createdDto;
        });

        // Act
        AuthorizationDto result = authorizationService.authorizeIfAuthorized(cardDto, ceilingDto, "generatedRef", BigDecimal.valueOf(100), authorizationRequest);

        // Assert
        assertNotNull(result);
        assertEquals(EnumAuthorizationStatus.GRANTED, result.getStatus());
        assertNotNull(result.getId());
        verify(cardService).updateCardStatus(cardDto.getId(), result.getId(), null, EnumCardStatus.AUTHORIZED);
    }
    @Test
    void createAuthorization_ConditionMet_NoCeilings() {
        // Arrange
        authorizationRequest = createAuthorizationRequest("supplier_ref", "sale_point_1", "card_tag", "productX");
        supplierDto = createSupplierDto("supplier_ref");
        salePointDto = SalePointDto.builder().identifier("sale_point_1").city("CityX").build();
        supplierDto.setSalePoints(Collections.singleton(salePointDto));
        when(supplierService.findByReference("supplier_ref")).thenReturn(supplierDto);

        CardDto cardDto = createActiveCard();
        when(cardService.findByTag("card_tag")).thenReturn(cardDto);

        CardGroupDto cardGroupDto = createCardGroupWithCondition("allowedProduct == 'productX'");
        cardGroupDto.setGroupCondition(GroupConditionDto.builder().allowedProduct("productX").build());
        cardGroupDto.setCeilings(Collections.emptySet());
        when(cardGroupService.checkedFindById(1L)).thenReturn(cardGroupDto);

        when(authorizationDao.findLastAuthorization()).thenReturn(null);
        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);

        // Act
        authorizationService.createAuthorization(authorizationRequest);

        // Assert
        verify(cardGroupService).findById(cardDto.getCardGroupId());
    }
    @Test
    void createAuthorization_ConditionMet_WithCeilings() {
        // Arrange
        authorizationRequest = createAuthorizationRequest("supplier_ref", "sale_point_1", "card_tag", "productX");
        supplierDto = createSupplierDto("supplier_ref");
        salePointDto = SalePointDto.builder().identifier("sale_point_1").city("CityX").build();
        supplierDto.setSalePoints(Collections.singleton(salePointDto));
        when(supplierService.findByReference("supplier_ref")).thenReturn(supplierDto);

        cardDto = createActiveCard();
        when(cardService.findByTag("card_tag")).thenReturn(cardDto);

        cardGroupDto = createCardGroupWithCondition("allowedProduct == 'productX'");
        cardGroupDto.setGroupCondition(GroupConditionDto.builder().allowedProduct("productX").build());

        ceilingDto = CeilingDto.builder().value(BigDecimal.TEN).build();
        cardGroupDto.setCeilings(Collections.singleton(ceilingDto));
        when(cardGroupService.checkedFindById(1L)).thenReturn(cardGroupDto);

        when(authorizationDao.findLastAuthorization()).thenReturn(null);
        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);

        when(transactionService.findLastTransactionByCardId(any(), any(), any())).thenReturn(Optional.empty());
        when(authorizationDao.create(any())).thenAnswer(invocation -> {
            return AuthorizationDto.builder().id(1L).status(EnumAuthorizationStatus.GRANTED).build();
        });

        // Act
        AuthorizationDto result = authorizationService.createAuthorization(authorizationRequest);

        // Assert
        assertEquals(EnumAuthorizationStatus.GRANTED, result.getStatus());
        verify(cardService).updateCardStatus(cardDto.getId(), result.getId(), null, EnumCardStatus.AUTHORIZED);
    }
    @Test
    void createAuthorization_ConditionNotMet() {
        // Arrange
        authorizationRequest = createAuthorizationRequest("supplier_ref", "sale_point_1", "card_tag", "productY");
        supplierDto = createSupplierDto("supplier_ref");
        salePointDto = SalePointDto.builder().identifier("sale_point_1").city("CityX").build();
        supplierDto.setSalePoints(Collections.singleton(salePointDto));

        when(supplierService.findByReference("supplier_ref")).thenReturn(supplierDto);

        cardDto = createActiveCard();
        when(cardService.findByTag("card_tag")).thenReturn(cardDto);

        cardGroupDto = createCardGroupWithCondition("allowedProduct == 'productX'");
        cardGroupDto.setGroupCondition(GroupConditionDto.builder().allowedProduct("productX").build());

        when(cardGroupService.checkedFindById(1L)).thenReturn(cardGroupDto);
        when(authorizationDao.findLastAuthorization()).thenReturn(null);
        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);

        // Act
        authorizationService.createAuthorization(authorizationRequest);

        // Assert
        verify(cardGroupService).findById(cardDto.getCardGroupId());
    }
    @Test
    void createAuthorization_ConditionMet_GrantAuthorization() {
        // Arrange
        authorizationRequest = createAuthorizationRequest("supplier_ref", "sale_point_1", "card_tag", "productX");

        supplierDto = createSupplierDto("sale_point_1");
        when(supplierService.findByReference("supplier_ref")).thenReturn(supplierDto);

        cardDto = createActiveCard();
        when(cardService.findByTag("card_tag")).thenReturn(cardDto);

        cardGroupDto = createCardGroupWithCondition("allowedProduct == 'productX'");
        when(cardGroupService.findById(1L)).thenReturn(Optional.of(cardGroupDto));

        ceilingDto = CeilingDto.builder().value(BigDecimal.TEN).build();
        cardGroupDto.setCeilings(Collections.singleton(ceilingDto));

        // Last authorization mock
        when(authorizationDao.findLastAuthorization()).thenReturn(null);
        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);

        // Act
        authorizationService.createAuthorization(authorizationRequest);

        verify(cardService).findByTag("card_tag");

    }
    @Test
    void createAuthorization_ConditionNotMet_SalePointNotAllowed() {
        authorizationRequest = createAuthorizationRequest("supplier_ref", "sale_point_2", "card_tag", "productX");
        supplierDto = createSupplierDto("sale_point_1");
        cardDto = createActiveCard();
        cardGroupDto = createCardGroupWithCondition("allowedProduct == 'productX' OR allowedSalePoints == 'sale_point_1'");

        when(supplierService.findByReference("supplier_ref")).thenReturn(supplierDto);
        when(cardService.findByTag("card_tag")).thenReturn(cardDto);
        when(cardGroupService.checkedFindById(1L)).thenReturn(cardGroupDto);
        when(authorizationDao.findLastAuthorization()).thenReturn(null);

        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);

        // Act
        authorizationService.createAuthorization(authorizationRequest);

        verify(supplierService).findByReference("supplier_ref");
        verify(authorizationDao).findLastAuthorization();
    }
    @Test
    void createAuthorization_NoCeilingAvailable() {
        // Arrange
        authorizationRequest = createAuthorizationRequest("supplier_ref", "sale_point_1", "card_tag", "productX");
        supplierDto = createSupplierDto("sale_point_1");
        cardDto = createActiveCard();

        // Create a card group with no ceilings or an empty ceilings list
        cardGroupDto = CardGroupDto.builder()
                .ceilings(Collections.emptySet())
                .build();

        when(supplierService.findByReference("supplier_ref")).thenReturn(supplierDto);
        when(cardService.findByTag("card_tag")).thenReturn(cardDto);
        when(cardGroupService.findById(1L)).thenReturn(Optional.ofNullable(cardGroupDto));
        when(authorizationDao.findLastAuthorization()).thenReturn(null);

        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);

        // Act
        authorizationService.createAuthorization(authorizationRequest);

        // Verify interactions
        verify(supplierService).findByReference("supplier_ref");
        verify(cardService).findByTag("card_tag");
        verify(cardGroupService).checkedFindById(1L);
        verify(authorizationDao).findLastAuthorization();
    }
    @Test
    void createAuthorization_FailedToUpdateCardStatus() {
        // Arrange
        authorizationRequest = createAuthorizationRequest("supplier_ref", "sale_point_1", "card_tag", "productX");
        supplierDto = createSupplierDto("sale_point_1");
        cardDto = createActiveCard();
        ceilingDto = mock(CeilingDto.class);
        when(ceilingDto.getValue()).thenReturn(BigDecimal.TEN);

        cardGroupDto = createCardGroupWithCondition("allowedProduct == 'productX' OR allowedSalePoints == 'sale_point_1'");

        cardGroupDto.setCeilings(Collections.singleton(ceilingDto));

        when(supplierService.findByReference("supplier_ref")).thenReturn(supplierDto);
        when(cardService.findByTag("card_tag")).thenReturn(cardDto);
        when(cardGroupService.findById(1L)).thenReturn(Optional.of(cardGroupDto));
        when(authorizationDao.findLastAuthorization()).thenReturn(null);

        when(validator.validateOnCreate(any())).thenReturn(mock(ESSValidationResult.class));
        when(validator.validateOnCreate(any()).hasErrors()).thenReturn(false);

        doThrow(new RuntimeException("Failed to update card status")).when(cardService).updateCardStatus(any(), any(), any(), any());

        // Act
        authorizationService.createAuthorization(authorizationRequest);

        verify(supplierService).findByReference("supplier_ref");
        verify(cardService).findByTag("card_tag");
        verify(cardGroupService).checkedFindById(1L);
        verify(authorizationDao).findLastAuthorization();
    }

    private AuthorizationRequest createAuthorizationRequest(String reference, String salePointIdentifier, String tag, String productName) {
        return AuthorizationRequest.builder()
                .reference(reference)
                .salePointIdentifier(salePointIdentifier)
                .tag(tag)
                .productName(productName)
                .build();
    }
    private SupplierDto createSupplierDto(String salePointIdentifier) {
        SalePointDto salePointDto = SalePointDto.builder()
                .identifier(salePointIdentifier)
                .build();
        return SupplierDto.builder()
                .salePoints(Collections.singleton(salePointDto))
                .build();
    }
    private CardDto createActiveCard() {
        return CardDto.builder()
                .id(1L)
                .expirationDate(LocalDate.now().plusDays(1))
                .status(EnumCardStatus.FREE)
                .actif(true)
                .cardGroupId(1L)
                .build();
    }
    private CardGroupDto createCardGroupWithCondition(String condition1) {
        GroupConditionDto groupConditionDto = GroupConditionDto.builder().build();
        groupConditionDto.setAllowedProduct(String.valueOf(Collections.singletonList("productX")));
        groupConditionDto.setAllowedSalePoints(String.valueOf(Collections.singletonList("sale_point_1")));

        return CardGroupDto.builder()
                .actif(true)
                .condition(condition1)
                .groupCondition(groupConditionDto)
                .ceilings(Collections.singleton(CeilingDto.builder().build()))
                .build();
    }

}