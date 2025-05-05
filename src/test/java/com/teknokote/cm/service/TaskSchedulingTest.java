package com.teknokote.cm.service;

import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.service.interfaces.CardService;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.cm.scheduling.TaskScheduling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskSchedulingTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private TaskScheduling taskScheduling;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkAndFreeStaleCards_shouldFreeStaleCards() {
        // Arrange
        LocalDateTime staleTime = LocalDateTime.now().minusMinutes(4);
        LocalDateTime recentTime = LocalDateTime.now();

        CardDto card1 = CardDto.builder().build();
        card1.setId(1L);
        card1.setCardId("Card1");
        card1.setStatus(EnumCardStatus.CONFIRMATION);
        card1.setDateStatusChange(staleTime);

        CardDto card2 = CardDto.builder().build();
        card2.setId(2L);
        card2.setCardId("Card2");
        card2.setStatus(EnumCardStatus.IN_USE);
        card2.setDateStatusChange(recentTime.minusMinutes(2)); // not stale

        when(cardService.findAll()).thenReturn(Arrays.asList(card1, card2));

        // Act
        taskScheduling.checkAndFreeStaleCards();

        // Assert
        verify(cardService).updateCardStatus(eq(card1.getId()), isNull(), isNull(), eq(EnumCardStatus.FREE));
        verify(cardService, never()).updateCardStatus(eq(card2.getId()), any(), any(), any()); // card2 should not be updated
    }

    @Test
    void checkAndFreeStaleCards_noStaleCards() {
        // Arrange
        LocalDateTime recentTime = LocalDateTime.now();

        CardDto card = CardDto.builder().build();
        card.setId(1L);
        card.setCardId("Card1");
        card.setStatus(EnumCardStatus.IN_USE);
        card.setDateStatusChange(recentTime.minusMinutes(2)); // not stale

        when(cardService.findAll()).thenReturn(Collections.singletonList(card));

        // Act
        taskScheduling.checkAndFreeStaleCards();

        // Assert
        verify(cardService, never()).updateCardStatus(anyLong(), any(), any(), any()); // no updates should have occurred
    }
}