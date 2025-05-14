package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.core.service.impl.CardGroupServiceImpl;
import com.teknokote.cm.dto.*;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ESSValidationResult;
import com.teknokote.core.service.ESSValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @Test
    void testCreateCardGroup_Success() {
        // Arrange
        CardGroupDto newGroup = CardGroupDto.builder().build();
        newGroup.setName("Test Group");
        newGroup.setCustomerId(1L);
        when(cardGroupDao.findByNameAndCustomerId(newGroup.getName(), newGroup.getCustomerId())).thenReturn(null);
        when(cardGroupDao.create(any(CardGroupDto.class))).thenReturn(newGroup);

        // Act
        CardGroupDto result = cardGroupService.createCardGroup(newGroup);

        // Assert
        assertNotNull(result);
        assertEquals("Test Group", result.getName());
    }
    @Test
    void testCreateCardGroup_DuplicateName() {
        // Arrange
        CardGroupDto newGroup = CardGroupDto.builder().build();
        newGroup.setName("Duplicate Group");
        newGroup.setCustomerId(1L);
        when(cardGroupDao.findByNameAndCustomerId(newGroup.getName(), newGroup.getCustomerId())).thenReturn(newGroup);

        // Act & Assert
        ServiceValidationException thrown = assertThrows(ServiceValidationException.class, () -> {
            cardGroupService.createCardGroup(newGroup);
        });
        assertEquals("Un groupe de carte avec le nom 'Duplicate Group' existe déja , veillier choisir un autre nom", thrown.getMessage());
    }
    @Test
    void testUpdateCardGroup_Success() {
        // Arrange
        CardGroupDto existingGroup = CardGroupDto.builder().build();
        existingGroup.setId(1L);
        existingGroup.setCustomerId(1L);
        existingGroup.setName("Old Name");

        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();

        when(validator.validateOnUpdate(existingGroup)).thenReturn(validationResult);

        when(cardGroupService.findById(existingGroup.getId())).thenReturn(Optional.of(existingGroup));
        when(cardGroupDao.findByNameAndCustomerId(existingGroup.getName(), existingGroup.getCustomerId())).thenReturn(null);
        when(cardGroupDao.findAllByCustomer(existingGroup.getCustomerId())).thenReturn(List.of(existingGroup));
        when(cardGroupDao.update(any(CardGroupDto.class))).thenReturn(existingGroup);

        // Act
        CardGroupDto updatedGroup = cardGroupService.updateCardGroup(existingGroup);

        // Assert
        assertEquals(existingGroup.getName(), updatedGroup.getName());
    }
    @Test
    void testUpdateCardGroup_DuplicateName() {
        // Arrange
        CardGroupDto existingGroup = CardGroupDto.builder().build();
        existingGroup.setId(1L);
        existingGroup.setCustomerId(1L);
        existingGroup.setName("Existing Group");

        // Mocking the return value for checking existing group by ID
        when(cardGroupDao.findById(existingGroup.getId())).thenReturn(Optional.of(existingGroup));

        // Mocking the return value to simulate existing cards associated with the customer
        CardGroupDto duplicateGroup = CardGroupDto.builder().build();
        duplicateGroup.setId(2L);
        duplicateGroup.setName("Existing Group");

        when(cardGroupDao.findAllByCustomer(existingGroup.getCustomerId()))
                .thenReturn(List.of(existingGroup, duplicateGroup));

        // Act & Assert
        ServiceValidationException thrown = assertThrows(ServiceValidationException.class, () -> {
            cardGroupService.updateCardGroup(existingGroup);
        });

        assertEquals("Un groupe de carte avec le nom 'Existing Group' existe déja , veillier choisir un autre nom", thrown.getMessage());
    }
    @Test
    void testTimeSlotsOverlap_NoOverlap() {
        // Arrange
        TimeSlotDto timeSlot1 = TimeSlotDto.builder().build();
        timeSlot1.setStartActivityTime(LocalTime.of(9, 0));
        timeSlot1.setEndActivityTime(LocalTime.of(10, 0));

        TimeSlotDto timeSlot2 = TimeSlotDto.builder().build();
        timeSlot2.setStartActivityTime(LocalTime.of(10, 0));
        timeSlot2.setEndActivityTime(LocalTime.of(11, 0));

        // Act & Assert
        assertDoesNotThrow(() -> cardGroupService.timeSlotsOverlap(List.of(timeSlot1, timeSlot2)));
    }
    @Test
    void testTimeSlotsOverlap_Overlap() {
        // Arrange
        TimeSlotDto timeSlot1 = TimeSlotDto.builder().build();
        timeSlot1.setStartActivityTime(LocalTime.of(9, 0));
        timeSlot1.setEndActivityTime(LocalTime.of(10, 0));

        TimeSlotDto timeSlot2 = TimeSlotDto.builder().build();
        timeSlot2.setStartActivityTime(LocalTime.of(9, 30));
        timeSlot2.setEndActivityTime(LocalTime.of(11, 0));

        // Act & Assert
        ServiceValidationException thrown = assertThrows(ServiceValidationException.class, () -> {
            cardGroupService.timeSlotsOverlap(List.of(timeSlot1, timeSlot2));
        });
        assertEquals("Overlap between time Slots ", thrown.getMessage());
    }
    @Test
    void testIsOverlap() {
        // Arrange
        TimeSlotDto timeSlot1 = TimeSlotDto.builder().build();
        timeSlot1.setStartActivityTime(LocalTime.of(9, 0));
        timeSlot1.setEndActivityTime(LocalTime.of(10, 0));

        TimeSlotDto timeSlot2 = TimeSlotDto.builder().build();
        timeSlot2.setStartActivityTime(LocalTime.of(9, 30));
        timeSlot2.setEndActivityTime(LocalTime.of(11, 0));

        // Act
        boolean overlap = cardGroupService.isOverlap(timeSlot1, timeSlot2);

        // Assert
        assertTrue(overlap);
    }
    @Test
    void testCardGroupInformation() {
        // Arrange
        Long cardGroupId = 1L;
        CardGroupDto dto = CardGroupDto.builder().build();
        dto.setGroupCondition(GroupConditionDto.builder().build());

        // Mock the behavior of checkedFindById to return the expected DTO
        when(cardGroupService.findById(cardGroupId)).thenReturn(Optional.of(dto));

        // Act
        CardGroupDto result = cardGroupService.cardGroupInformation(cardGroupId);

        // Assert that the returned DTO is the same as the one we set up
        assertEquals(dto, result);
    }
    @Test
    void testUpdateCardGroup_WithCeilingValidation() {
        // Arrange
        CardGroupDto dto = CardGroupDto.builder().build();
        dto.setId(1L);
        dto.setCustomerId(1L);
        dto.setName("Test Group");

        CeilingDto ceilingDto = CeilingDto.builder().build();
        ceilingDto.setValue(BigDecimal.ZERO);
        dto.setCeilings(Set.of(ceilingDto));

        // Mock the behavior of checkedFindById to allow update
        when(cardGroupService.findById(dto.getId())).thenReturn(Optional.of(dto));

        // Act & Assert
        ServiceValidationException thrown = assertThrows(ServiceValidationException.class, () -> {
            cardGroupService.updateCardGroup(dto);
        });

        assertEquals("limit  value must be greater than 0 !", thrown.getMessage());
    }
    @Test
    void testUpdateCardGroup_WithTimeSlotsOverlap() {
        // Arrange
        CardGroupDto dto = CardGroupDto.builder().build();
        dto.setId(1L);
        dto.setCustomerId(1L);
        dto.setName("Test Group");

        GroupConditionDto groupCondition = GroupConditionDto.builder().build();
        TimeSlotDto timeSlot1 = TimeSlotDto.builder().build(); // Overlapping
        timeSlot1.setStartActivityTime(LocalTime.of(9, 0));
        timeSlot1.setEndActivityTime(LocalTime.of(10, 0));

        TimeSlotDto timeSlot2 = TimeSlotDto.builder().build(); // Overlapping
        timeSlot2.setStartActivityTime(LocalTime.of(9, 30));
        timeSlot2.setEndActivityTime(LocalTime.of(11, 0));

        groupCondition.setTimeSlots(List.of(timeSlot1, timeSlot2));
        dto.setGroupCondition(groupCondition);

        // Mock the behavior of checkedFindById to allow update
        when(cardGroupService.findById(dto.getId())).thenReturn(Optional.of(dto));

        // Act & Assert
        ServiceValidationException thrown = assertThrows(ServiceValidationException.class, () -> {
            cardGroupService.updateCardGroup(dto);
        });

        assertEquals("Overlap between time Slots ", thrown.getMessage());
    }
    @Test
    void testUpdateCardGroup_SuccessWithValidData() {
        // Arrange
        CardGroupDto dto = CardGroupDto.builder().build();
        dto.setId(1L);
        dto.setCustomerId(1L);
        dto.setName("Valid Group");

        CeilingDto ceilingDto = CeilingDto.builder().build();
        ceilingDto.setValue(new BigDecimal("100"));
        dto.setCeilings(Set.of(ceilingDto));

        TimeSlotDto timeSlot1 = TimeSlotDto.builder().build();
        timeSlot1.setStartActivityTime(LocalTime.of(10, 0));
        timeSlot1.setEndActivityTime(LocalTime.of(11, 0));

        TimeSlotDto timeSlot2 = TimeSlotDto.builder().build();
        timeSlot2.setStartActivityTime(LocalTime.of(11, 0));
        timeSlot2.setEndActivityTime(LocalTime.of(12, 0));

        GroupConditionDto groupCondition = GroupConditionDto.builder().build();
        groupCondition.setTimeSlots(List.of(timeSlot1, timeSlot2));
        dto.setGroupCondition(groupCondition);

        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();

        when(validator.validateOnUpdate(dto)).thenReturn(validationResult);

        // Mock the behavior of checkedFindById to allow update
        when(cardGroupService.findById(dto.getId())).thenReturn(Optional.of(dto));
        when(cardGroupDao.findByNameAndCustomerId(dto.getName(), dto.getCustomerId())).thenReturn(null);
        when(cardGroupDao.update(any(CardGroupDto.class))).thenReturn(dto);

        // Act
        CardGroupDto updatedDto = cardGroupService.updateCardGroup(dto);

        // Assert
        assertEquals(dto.getName(), updatedDto.getName());

    }
}