package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.core.service.CardGroupService;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.cm.dto.CeilingDto;
import com.teknokote.cm.dto.TimeSlotDto;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Service
@Getter
public class CardGroupServiceImpl extends ActivatableGenericCheckedService<Long, CardGroupDto> implements CardGroupService
{
    @Autowired
    private ESSValidator<CardGroupDto> validator;
    @Autowired
    private CardGroupDao dao;
    @Autowired
    private CardDao cardDao;

    @Override
    public List<CardGroupDto> findAllByActif(boolean actif){
        return getDao().findAllByActif(actif);
    }

    @Override
    public List<CardGroupDto> findAllByCustomer(Long customerId) {
        List<CardGroupDto> cardGroups = getDao().findAllByCustomer(customerId);

        for (CardGroupDto cardGroup : cardGroups) {
            List<CardDto> cards = cardDao.findAllByCardGroupId(cardGroup.getId());
            // Set the cardCount for the cardGroup
            cardGroup.setCardCount(cards.size());
        }
        return cardGroups;
    }


    @Override
    public CardGroupDto cardGroupInformation(Long cardGroupId) {
        CardGroupDto dto = checkedFindById(cardGroupId);
        if (dto.getGroupCondition() != null) {
            if (dto.getGroupCondition().getTimeSlot() != null) {
                dto.getGroupCondition().setTimeSlots(dto.createListTimeSlotsFromString());
            }
            if (dto.getGroupCondition().getLogicalOperators() != null){
                dto.getGroupCondition().setOperators(dto.createListOperatorFromString());
            }
        }
        return dto;
    }

    @Override
    public CardGroupDto updateCardGroup(CardGroupDto dto) {
        CardGroupDto cardGroupDto = checkedFindById(dto.getId());
        dto.createConditionFromGroupCondition();
        List<TimeSlotDto> timeSlots = dto.getGroupCondition().getTimeSlots();
        if (timeSlots!=null && !timeSlots.isEmpty()){
            timeSlotsOverlap(timeSlots);
        }
        if (dto.getCeilings()!=null && !dto.getCeilings().isEmpty()){
            for (CeilingDto ceilingDto:dto.getCeilings()){
                if (ceilingDto.getDailyLimitValue().equals(BigDecimal.ZERO)){
                    throw new ServiceValidationException("daily limit value must be greater than 0 !");
                }
                if (ceilingDto.getValue().equals(BigDecimal.ZERO)){
                    throw new ServiceValidationException("Monthly limit  value must be greater than 0 !");
                }
                if (ceilingDto.getValue().compareTo(ceilingDto.getDailyLimitValue())<=0){
                    throw new ServiceValidationException("Monthly limit  value must be greater than the daily limit value  !");
                }
            }
        }
        return update(dto);
    }

    public void timeSlotsOverlap(List<TimeSlotDto> timeSlots) {
        for (int i = 0; i < timeSlots.size(); i++) {
            for (int j = i + 1; j < timeSlots.size(); j++) {
                if (isOverlap(timeSlots.get(i), timeSlots.get(j))) {
                    throw new ServiceValidationException("Overlap between time Slots ");
                }
            }
        }
    }
    public boolean isOverlap(TimeSlotDto firstTimeSlot, TimeSlotDto secondTimeSlot) {
        LocalTime end1 = firstTimeSlot.getEndActivityTime();
        LocalTime start2 = secondTimeSlot.getStartActivityTime();
        return start2.isBefore(end1);
    }
}

