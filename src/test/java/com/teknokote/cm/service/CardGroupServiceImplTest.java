package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.core.service.impl.CardGroupServiceImpl;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.service.ESSValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CardGroupServiceImplTest {

    @InjectMocks
    private CardGroupServiceImpl cardGroupService;
    @Mock
    private CardGroupDao cardGroupDao;
    @Mock
    private CardDao cardDao;
    @Mock
    private ESSValidator<CardGroupDto> validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllByActif() {
        // Arrange
        boolean actif = true;
        List<CardGroupDto> expectedCardGroups = List.of(CardGroupDto.builder().build());
        when(cardGroupDao.findAllByActif(actif)).thenReturn(expectedCardGroups);

        // Act
        List<CardGroupDto> actualCardGroups = cardGroupService.findAllByActif(actif);

        // Assert
        assertEquals(expectedCardGroups, actualCardGroups);
    }
    @Test
    void testFindAllByCustomer() {
        // Arrange
        Long customerId = 1L;
        CardGroupDto cardGroupDto = CardGroupDto.builder().build();
        cardGroupDto.setId(1L);
        when(cardGroupDao.findAllByCustomer(customerId)).thenReturn(List.of(cardGroupDto));
        when(cardDao.findAllByCardGroupId(cardGroupDto.getId())).thenReturn(List.of(CardDto.builder().build()));

        // Act
        List<CardGroupDto> result = cardGroupService.findAllByCustomer(customerId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCardCount()); // Check that card count was set correctly
    }

}