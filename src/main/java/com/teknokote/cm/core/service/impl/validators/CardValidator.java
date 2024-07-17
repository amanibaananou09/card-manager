package com.teknokote.cm.core.service.impl.validators;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ESSValidationResult;
import com.teknokote.core.service.ESSValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Component
public class CardValidator implements ESSValidator<CardDto>
{
    @Autowired
    private CardDao dao;
    @Autowired
    private CardGroupDao cardGroupDao;
    @Override
    public ESSValidationResult validateOnCreate(CardDto dto) {
        final ESSValidationResult results = ESSValidator.super.validate(dto);
        Optional<CardGroupDto> cardGroupDto=cardGroupDao.findById(dto.getCardGroupId());
        if (cardGroupDto.isPresent()) {
            final Optional<CardDto> cardDto = dao.findCardById(cardGroupDto.get().getCustomerId(), dto.getCardId());
            cardDto.ifPresent(cardDto1 -> {throw new ServiceValidationException("L'ID de la carte de carburant que vous avez choisi est déjà attribué. Veuillez choisir un ID unique");});
        }
        return results;
    }

    @Override
    public ESSValidationResult validateOnUpdate(CardDto dto) {
        final ESSValidationResult results = ESSValidator.super.validate(dto);
        Optional<CardGroupDto> cardGroupDto=cardGroupDao.findById(dto.getCardGroupId());
        if (cardGroupDto.isPresent()) {
            final List<CardDto> cardDtoList= dao.findAllByCardGroupId(dto.getCardGroupId()).stream().filter(cardDto -> !cardDto.getId().equals(dto.getId())).toList();
            if (isNotEmpty(cardDtoList)){
                for (CardDto card : cardDtoList){
                    if (card.getCardId().equals(dto.getCardId())){
                        throw new ServiceValidationException("L'ID de la carte de carburant que vous avez choisi est déjà attribué. Veuillez choisir un ID unique");
                    }
                }
            }
        }
        return results;
    }
}
