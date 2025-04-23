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
import java.util.Comparator;
import java.util.List;

@Service
@Getter
public class CardGroupServiceImpl extends ActivatableGenericCheckedService<Long, CardGroupDto> implements CardGroupService {
    @Autowired
    private ESSValidator<CardGroupDto> validator;
    @Autowired
    private CardGroupDao dao;
    @Autowired
    private CardDao cardDao;

    @Override
    public List<CardGroupDto> findAllByActif(boolean actif) {
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
    public CardGroupDto createCardGroup(CardGroupDto cardGroupDto){
        CardGroupDto cardGroup = getDao().findByNameAndCustomerId(cardGroupDto.getName(),cardGroupDto.getCustomerId());
        if (cardGroup!=null){
            throw new ServiceValidationException("Un groupe de carte avec le nom "+ "'"+cardGroup.getName()+"' "+"existe déja , veillier choisir un autre nom" );
        }else{
            return getDao().create(cardGroupDto);
        }
    }


    @Override
    public CardGroupDto cardGroupInformation(Long cardGroupId) {
        CardGroupDto dto = checkedFindById(cardGroupId);
        if (dto.getGroupCondition() != null) {
            if (dto.getGroupCondition().getTimeSlot() != null) {
                dto.getGroupCondition().setTimeSlots(dto.createListTimeSlotsFromString());
            }
            if (dto.getGroupCondition().getLogicalOperators() != null) {
                dto.getGroupCondition().setOperators(dto.createListOperatorFromString());
            }
        }
        return dto;
    }

    @Override
    public CardGroupDto updateCardGroup(CardGroupDto dto) {
        checkedFindById(dto.getId());
        List<CardGroupDto> cardGroups = getDao().findAllByCustomer(dto.getCustomerId()).stream().filter(cardGroupDto->!cardGroupDto.getId().equals(dto.getId())).toList();
        for (CardGroupDto cardGroupDto : cardGroups){
            if (cardGroupDto.getName().equals(dto.getName())){
                throw new ServiceValidationException("Un groupe de carte avec le nom "+ "'"+dto.getName()+"' "+"existe déja , veillier choisir un autre nom" );
            }
        }
        dto.createConditionFromGroupCondition();
        if (dto.getGroupCondition() != null) {
            List<TimeSlotDto> timeSlots = dto.getGroupCondition().getTimeSlots();

            if (timeSlots != null && !timeSlots.isEmpty()) {
                timeSlotsOverlap(timeSlots);
            }
        }
        if (dto.getCeilings() != null && !dto.getCeilings().isEmpty()) {
            for (CeilingDto ceilingDto : dto.getCeilings()) {
                if (ceilingDto.getValue().equals(BigDecimal.ZERO)) {
                    throw new ServiceValidationException("limit  value must be greater than 0 !");
                }
            }
        }
        return update(dto);
    }

    public void timeSlotsOverlap(List<TimeSlotDto> timeSlots) {
        List<TimeSlotDto> timeSlotsList = timeSlots.stream().sorted(Comparator
                .comparing(TimeSlotDto::getStartActivityTime)).toList();
        for (int i = 0; i < timeSlotsList.size(); i++) {
            for (int j = i + 1; j < timeSlotsList.size(); j++) {
                if (isOverlap(timeSlotsList.get(i), timeSlotsList.get(j))) {
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

