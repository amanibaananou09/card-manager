package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.CardMovementHistoryDao;
import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.service.CardService;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.cm.dto.CardMovementHistoryDto;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidationResult;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Getter
public class CardServiceImpl extends ActivatableGenericCheckedService<Long, CardDto> implements CardService {
    @Autowired
    private ESSValidator<CardDto> validator;
    @Autowired
    private CardDao dao;
    @Autowired
    private AuthorizationDao authorizationDao;
    @Autowired
    private CardMovementHistoryDao cardMovementHistoryDao;

    @Override
    public List<CardDto> findAllByCustomer(Long customerId) {
        return getDao().findAllByCustomer(customerId);
    }


    @Override
    public List<CardDto> findCardByFilter(Long customerId, String holder, String cardId, LocalDate expirationDate, Boolean actif) {

        List<CardDto> cardDtoList = null;

        if (expirationDate != null) {
            int year = expirationDate.getYear();
            int month = expirationDate.getMonthValue();
            cardDtoList = getDao().findCardByExpirationDate(month, year, customerId);
        } else if (holder != null) {
            cardDtoList = getDao().findCardByHolderName(holder, customerId);
        } else if (cardId != null) {
            cardDtoList = getDao().findCardByCardId(cardId, customerId);
        } else if (actif != null) {
            cardDtoList = getDao().findAllByActifAndCustomer(actif, customerId);
        } else {
            cardDtoList = getDao().findAllByCustomer(customerId);
        }
        return cardDtoList;
    }

    @Override
    public CardDto findByTag(String tag) {
        return getDao().findByTag(tag);
    }

    @Override
    public void updateCardStatus(Long cardId,Long authorizationId,Long transactionId, EnumCardStatus status) {
        CardDto cardDto = checkedFindById(cardId);
        if (cardDto != null) {
            CardMovementHistoryDto cardMovementHistoryDto = CardMovementHistoryDto.builder().cardId(cardDto.getId()).authorizationId(authorizationId)
                    .ctrTransactionRef(transactionId).dateTime(LocalDateTime.now()).oldStatus(cardDto.getStatus()).newStatus(status).build();
            cardMovementHistoryDao.create(cardMovementHistoryDto);
            cardDto.setStatus(status);
            update(cardDto);
        }
    }
    @Override
    public CardDto create(CardDto dto) {
        final ESSValidationResult cardValidation = getValidator().validateOnCreate(dto);
        if (cardValidation.hasErrors()) {
            throw new ServiceValidationException(cardValidation.getMessage());
        }
        return super.create(dto);
    }
    @Override
    public CardDto update(CardDto dto) {
        final ESSValidationResult cardValidation = getValidator().validateOnUpdate(dto);
        if (cardValidation.hasErrors()) {
            throw new ServiceValidationException(cardValidation.getMessage());
        }
        return super.update(dto);
    }
}


