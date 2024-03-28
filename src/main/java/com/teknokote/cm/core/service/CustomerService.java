package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;

public interface CustomerService extends ActivatableEntityService<Long, CustomerDto>, BaseService<Long, CustomerDto>
{

}
