package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.dao.mappers.CardMapper;
import com.teknokote.cm.core.model.Card;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.CardRepository;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class CardDaoImpl extends JpaActivatableGenericDao<Long,User ,CardDto, Card> implements CardDao
{
    @Autowired
    private CardMapper mapper;
    @Autowired
    private CardRepository repository;
}
