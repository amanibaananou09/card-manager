package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;
import java.util.List;

public interface CardService extends ActivatableEntityService<Long, CardDto>, BaseService<Long, CardDto>
{
    List<CardDto> findAllByCustomer(Long customerId);
}
