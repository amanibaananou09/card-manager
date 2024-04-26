package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.mappers.CardMapper;
import com.teknokote.cm.core.model.Card;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.CardRepository;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Getter
@Setter
public class CardDaoImpl extends JpaActivatableGenericDao<Long,User ,CardDto, Card> implements CardDao
{
    @Autowired
    private CardMapper mapper;
    @Autowired
    private CardRepository repository;

    @Override
    public List<CardDto> findAllByCustomer(Long customerId) {
        return getRepository().findAllByCustomerId(customerId).stream().map(getMapper()::toDto).collect(Collectors.toList());
    }
}
