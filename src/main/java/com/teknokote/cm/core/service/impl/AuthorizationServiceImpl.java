package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.service.AuthorizationService;
import com.teknokote.cm.dto.AuthorizationDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AuthorizationServiceImpl extends GenericCheckedService<Long, AuthorizationDto> implements AuthorizationService
{
   @Autowired
   private ESSValidator<AuthorizationDto> validator;
   @Autowired
   private AuthorizationDao dao;
}
