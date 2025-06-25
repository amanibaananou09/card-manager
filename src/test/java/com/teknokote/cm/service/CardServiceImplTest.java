package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.CardMovementHistoryDao;
import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.service.impl.CardServiceImpl;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.cm.dto.CardMovementHistoryDto;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ESSValidationResult;
import com.teknokote.core.service.ESSValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl cardService;
    @Mock
    private CardDao cardDao;
    @Mock
    private AuthorizationDao authorizationDao;
    @Mock
    private CardMovementHistoryDao cardMovementHistoryDao;
    @Mock
    private ESSValidator<CardDto> validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCard_ValidDto() {
        // Arrange
        CardDto cardDto = CardDto.builder().build();
        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasNoErrors(); // No errors

        when(validator.validateOnCreate(cardDto)).thenReturn(validationResult);
        when(cardDao.create(cardDto)).thenReturn(cardDto); // Mock the create persistence logic

        // Act
        CardDto createdCard = cardService.create(cardDto);

        // Assert
        assertEquals(cardDto, createdCard);
    }
    @Test
    void testCreateCard_InvalidDto() {
        // Arrange
        CardDto cardDto = CardDto.builder().build();
        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();
        validationResult.foundFormatError("Validation errors found.");

        when(validator.validateOnCreate(cardDto)).thenReturn(validationResult);

        // Act & Assert
        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> {
            cardService.create(cardDto);
        });
        assertTrue(exception.getMessage().contains("Validation errors found."));
    }
    @Test
    void testFindByTag() {
        // Arrange
        String tag = "TAG123";
        CardDto expectedCard = CardDto.builder().build(); // Create and set properties of CardDto
        when(cardDao.findByTag(tag)).thenReturn(expectedCard);

        // Act
        CardDto actualCard = cardService.findByTag(tag);

        // Assert
        assertEquals(expectedCard, actualCard);
    }
    @Test
    void testFindCardByFilter_HolderName() {
        // Arrange
        Long customerId = 1L;
        String holderName = "John Doe";
        CardDto cardDto = CardDto.builder().build(); // Create and set properties of CardDto
        List<CardDto> expectedCards = List.of(cardDto);
        when(cardDao.findCardByHolderName(holderName, customerId)).thenReturn(expectedCards);

        // Act
        List<CardDto> actualCards = cardService.findCardByFilter(customerId, holderName, null, null, null);

        // Assert
        assertEquals(expectedCards, actualCards);
    }
    @Test
    void testFindCardByFilter_CardId() {
        // Arrange
        Long customerId = 1L;
        String cardId = "CARD123";
        CardDto cardDto = CardDto.builder().build(); // Create and set properties of CardDto
        List<CardDto> expectedCards = List.of(cardDto);
        when(cardDao.findCardByCardId(cardId, customerId)).thenReturn(expectedCards);

        // Act
        List<CardDto> actualCards = cardService.findCardByFilter(customerId, null, cardId, null, null);

        // Assert
        assertEquals(expectedCards, actualCards);
    }
    @Test
    void testFindCardByFilter_ActiveFlag() {
        // Arrange
        Long customerId = 1L;
        Boolean actif = true;
        CardDto cardDto = CardDto.builder().build(); // Create and set properties of CardDto
        List<CardDto> expectedCards = List.of(cardDto);
        when(cardDao.findAllByActifAndCustomer(actif, customerId)).thenReturn(expectedCards);

        // Act
        List<CardDto> actualCards = cardService.findCardByFilter(customerId, null, null, null, actif);

        // Assert
        assertEquals(expectedCards, actualCards);
    }
    @Test
    void testFindCardByFilter_ExpirationDate() {
        // Arrange
        Long customerId = 1L;
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        CardDto cardDto = CardDto.builder().build(); // Create and set properties of CardDto
        List<CardDto> expectedCards = List.of(cardDto);
        when(cardDao.findCardByExpirationDate(expirationDate.getMonthValue(), expirationDate.getYear(), customerId)).thenReturn(expectedCards);

        // Act
        List<CardDto> actualCards = cardService.findCardByFilter(customerId, null, null, expirationDate, null);

        // Assert
        assertEquals(expectedCards, actualCards);
    }
    @Test
    void testFindCardByFilter_NoFilters() {
        // Arrange
        Long customerId = 1L;
        CardDto cardDto = CardDto.builder().build(); // Create and set properties of CardDto
        List<CardDto> expectedCards = List.of(cardDto);
        when(cardDao.findAllByCustomer(customerId)).thenReturn(expectedCards);

        // Act
        List<CardDto> actualCards = cardService.findCardByFilter(customerId, null, null, null, null);

        // Assert
        assertEquals(expectedCards, actualCards);
    }
    @Test
    void testUpdateCard_ValidDto() {
        // Arrange
        CardDto cardDto = CardDto.builder().id(1L).build();
        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasNoErrors();

        when(validator.validateOnUpdate(cardDto)).thenReturn(validationResult);
        when(cardDao.update(cardDto)).thenReturn(cardDto); // Mock the update behavior

        // Act
        CardDto updatedCard = cardService.update(cardDto);

        // Assert
        assertEquals(cardDto, updatedCard);
        verify(cardDao, times(1)).update(cardDto);
    }
    @Test
    void testUpdateCard_InvalidDto() {
        // Arrange
        CardDto cardDto = CardDto.builder().build();
        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();
        validationResult.foundFormatError("Validation errors found.");

        when(validator.validateOnUpdate(cardDto)).thenReturn(validationResult);

        // Act & Assert
        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> {
            cardService.update(cardDto);
        });
        assertTrue(exception.getMessage().contains("Validation errors found."));
    }
    @Test
    void testUpdateCardStatus() {
        // Arrange
        Long cardId = 1L;
        Long authorizationId = 100L;
        Long transactionId = 200L;
        EnumCardStatus newStatus = EnumCardStatus.FREE;

        CardDto cardDto = CardDto.builder().id(cardId).status(EnumCardStatus.BLOCKED).build();
        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasNoErrors(); // Make sure there are no validation errors

        when(validator.validateOnUpdate(cardDto)).thenReturn(validationResult); // Mock validation

        when(cardDao.findById(cardId)).thenReturn(Optional.of(cardDto));
        when(cardMovementHistoryDao.create(any(CardMovementHistoryDto.class))).thenReturn(null); // Mock the create behavior

        // Act
        cardService.updateCardStatus(cardId, authorizationId, transactionId, newStatus);

        // Assert
        verify(cardMovementHistoryDao, times(1)).create(any(CardMovementHistoryDto.class));
        verify(cardDao, times(1)).update(cardDto);
        assertEquals(newStatus, cardDto.getStatus());
    }
}