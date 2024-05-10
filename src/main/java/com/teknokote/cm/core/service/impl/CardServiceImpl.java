package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.service.CardService;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Getter
public class CardServiceImpl extends ActivatableGenericCheckedService<Long, CardDto> implements CardService {
    @Autowired
    private ESSValidator<CardDto> validator;
    @Autowired
    private CardDao dao;

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

}

