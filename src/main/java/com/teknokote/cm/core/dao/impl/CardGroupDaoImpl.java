package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.core.dao.mappers.CardGroupMapper;
import com.teknokote.cm.core.model.CardGroup;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.CardGroupRepository;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class CardGroupDaoImpl extends JpaActivatableGenericDao<Long,User ,CardGroupDto, CardGroup> implements CardGroupDao
{
    @Autowired
    private CardGroupMapper mapper;
    @Autowired
    private CardGroupRepository repository;
}
