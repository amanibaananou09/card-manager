package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;

import java.util.List;

public interface CardGroupService extends ActivatableEntityService<Long, CardGroupDto>, BaseService<Long, CardGroupDto>
{
    List<CardGroupDto> findAllByActif(boolean actif);

    List<CardGroupDto> findAllByCustomer(Long customerId);
    CardGroupDto cardGroupInformation(Long id);
    CardGroupDto updateCardGroup(CardGroupDto dto);
}
