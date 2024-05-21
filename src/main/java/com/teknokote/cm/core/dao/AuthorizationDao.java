package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.AuthorizationDto;
import com.teknokote.core.dao.BasicDao;


public interface AuthorizationDao extends BasicDao<Long, AuthorizationDto>
{
    AuthorizationDto findLastAuthorization();

    AuthorizationDto findByReference(String authorizationReference);
}

