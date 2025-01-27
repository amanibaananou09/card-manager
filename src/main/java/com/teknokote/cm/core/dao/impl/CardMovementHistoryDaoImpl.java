package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CardMovementHistoryDao;
import com.teknokote.cm.core.dao.mappers.CardMovementHistoryMapper;
import com.teknokote.cm.core.model.Authorization;
import com.teknokote.cm.core.model.Card;
import com.teknokote.cm.core.model.CardMovementHistory;
import com.teknokote.cm.core.repository.CardMovementHistoryRepository;
import com.teknokote.cm.dto.CardMovementHistoryDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Getter
public class CardMovementHistoryDaoImpl extends JpaGenericDao<Long, CardMovementHistoryDto, CardMovementHistory> implements CardMovementHistoryDao {

    @Autowired
    private CardMovementHistoryMapper mapper;
    @Autowired
    private CardMovementHistoryRepository repository;

    @Override
    protected CardMovementHistory beforeCreate(CardMovementHistory cardMovementHistory,CardMovementHistoryDto dto){
        if (Objects.nonNull(dto.getAuthorizationId())){
            cardMovementHistory.setAuthorization(getEntityManager().getReference(Authorization.class,dto.getAuthorizationId()));
        }
        if (Objects.nonNull(dto.getDescription())){
            cardMovementHistory.setCard(getEntityManager().getReference(Card.class,dto.getCardId()));
        }
        return super.beforeCreate(cardMovementHistory,dto);
    }
    @Override
    public List<CardMovementHistoryDto> findByCustomerAndCardId(Long customerId, Long id) {
        return getRepository().findByCustomerAndCardId(customerId,id).stream().map(getMapper()::toDto).collect(Collectors.toList());
    }
}

