package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.dao.ActivatableDao;

import java.util.List;
import java.util.Optional;

public interface CardDao extends ActivatableDao<Long, CardDto> {
    List<CardDto> findAllByCustomer(Long customerId);

    List<CardDto> findAllByActifAndCustomer(Boolean actif, Long customerId);

    List<CardDto> findAllByCardGroupId(Long cardGroupId);

    List<CardDto> findCardByHolderName(String holder, Long customerId);

    List<CardDto> findCardByCardId(String cardId, Long customerId);

    List<CardDto> findCardByExpirationDate(int month, int year, Long customerId);

    CardDto findByTag(String tag);

    Optional<CardDto> findCardById(Long customerId,String cardId);
}

