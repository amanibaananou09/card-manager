package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.mappers.CardMapper;
import com.teknokote.cm.core.model.Account;
import com.teknokote.cm.core.model.Card;
import com.teknokote.cm.core.model.CardGroup;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.CardRepository;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Getter
@Setter
public class CardDaoImpl extends JpaActivatableGenericDao<Long, User, CardDto, Card> implements CardDao {
    @Autowired
    private CardMapper mapper;
    @Autowired
    private CardRepository repository;

    @Override
    public List<CardDto> findAllByCustomer(Long customerId) {
        return getRepository().findAllByCustomerId(customerId).stream().map(getMapper()::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardDto> findAllByActifAndCustomer(Boolean actif, Long customerId) {
        return getRepository().findAllByActifAndCustomer(actif, customerId).stream().map(getMapper()::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardDto> findCardByHolderName(String holder, Long customerId) {
        return getRepository().findCardByHolderName(holder, customerId).stream().map(getMapper()::toDto).collect(Collectors.toList());

    }

    @Override
    public List<CardDto> findCardByCardId(String cardId, Long customerId) {
        return getRepository().findCardByCardId(cardId, customerId).stream().map(getMapper()::toDto).collect(Collectors.toList());

    }

    @Override
    public List<CardDto> findCardByExpirationDate(int month, int year, Long customerId) {
        return getRepository().findCardByMonthAndYear(month, year, customerId)
                .stream()
                .map(getMapper()::toDto)
                .collect(Collectors.toList());
    }

    @Override
    protected Card beforeCreate(Card card, CardDto dto) {
        card.setActif(true);
        card.setDateStatusChange(LocalDateTime.now());

        if (Objects.nonNull(dto.getCardGroupId())) {
            card.setCardGroup(getEntityManager().getReference(CardGroup.class, dto.getCardGroupId()));
        }
        if (Objects.nonNull(dto.getAccountId())) {
            card.setAccount(getEntityManager().getReference(Account.class, dto.getAccountId()));
        }
        return super.beforeCreate(card, dto);
    }

    @Override
    protected Card beforeUpdate(Card card, CardDto dto) {
        card.setDateStatusChange(LocalDateTime.now());
        if (Objects.nonNull(dto.getCardGroupId())) {
            card.setCardGroup(getEntityManager().getReference(CardGroup.class, dto.getCardGroupId()));
        }
        if (Objects.nonNull(dto.getAccountId())) {
            card.setAccount(getEntityManager().getReference(Account.class, dto.getAccountId()));
        }
        return super.beforeUpdate(card, dto);
    }

    @Override
    public CardDto findByTag(String tag) {
        return getMapper().toDto(getRepository().findByTag(tag));
    }
}
