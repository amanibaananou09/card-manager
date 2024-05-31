package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.core.service.CardGroupService;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.cm.dto.TimeSlotDto;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<CardGroupDto> findAllByActif(boolean actif){
        return getDao().findAllByActif(actif);
    }

    @Override
    public List<CardGroupDto> findAllByCustomer(Long customerId) {
        return getDao().findAllByCustomer(customerId);
    }

    @Override
    public CardGroupDto cardGroupInformation(Long cardGroupId) {
        CardGroupDto dto = checkedFindById(cardGroupId);
        if (dto.getGroupCondition() != null){
            dto.getGroupCondition().setTimeSlots(dto.createListTimeSlotsFromString());
            dto.getGroupCondition().setOperators(dto.createListOperatorFromString());
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

