package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.BonusDao;
import com.teknokote.cm.core.dao.mappers.BonusMapper;
import com.teknokote.cm.core.model.Bonus;
import com.teknokote.cm.core.repository.BonusRepository;
import com.teknokote.cm.dto.BonusDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class BonusDaoImpl extends JpaGenericDao<Long,BonusDto, Bonus> implements BonusDao
{
    @Autowired
    private BonusMapper mapper;
    @Autowired
    private BonusRepository repository;
}
