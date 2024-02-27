package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.dao.mappers.AuthorizationMapper;
import com.teknokote.cm.core.model.Authorization;
import com.teknokote.cm.core.repository.AuthorizationRepository;
import com.teknokote.cm.dto.AuthorizationDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class AuthorizationDaoImpl extends JpaGenericDao<Long,AuthorizationDto, Authorization> implements AuthorizationDao
{
    @Autowired
    private AuthorizationMapper mapper;
    @Autowired
    private AuthorizationRepository repository;
}
