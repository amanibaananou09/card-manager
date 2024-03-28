package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CeilingDao;
import com.teknokote.cm.core.dao.mappers.CeilingMapper;
import com.teknokote.cm.core.model.Ceiling;
import com.teknokote.cm.core.repository.CeilingRepository;
import com.teknokote.cm.dto.CeilingDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class CeilingDaoImpl extends JpaGenericDao<Long,CeilingDto, Ceiling> implements CeilingDao
{
    @Autowired
    private CeilingMapper mapper;
    @Autowired
    private CeilingRepository repository;
}
