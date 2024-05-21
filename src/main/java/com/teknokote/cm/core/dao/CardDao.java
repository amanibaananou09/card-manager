package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.dao.ActivatableDao;

import java.util.List;

public interface CardDao extends ActivatableDao<Long, CardDto> {
    List<CardDto> findAllByCustomer(Long customerId);

    List<CardDto> findAllByActifAndCustomer(Boolean actif, Long customerId);

    List<CardDto> findCardByHolderName(String holder, Long customerId);

    List<CardDto> findCardByCardId(String cardId, Long customerId);

    List<CardDto> findCardByExpirationDate(int month, int year, Long customerId);

    CardDto findByTag(String tag);
}

