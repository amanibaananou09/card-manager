package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.AuthorizationDto;
import com.teknokote.cm.dto.AuthorizationRequest;
import com.teknokote.core.service.BaseService;

public interface AuthorizationService extends BaseService<Long, AuthorizationDto>
{
    AuthorizationDto createAuthorization (AuthorizationRequest authorizationRequest);

    AuthorizationDto findByPtsIdAndPump(String ptsId, Long pump);
}
