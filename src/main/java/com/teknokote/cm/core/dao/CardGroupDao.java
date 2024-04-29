package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.dao.ActivatableDao;

import java.util.List;


public interface CardGroupDao extends ActivatableDao<Long, CardGroupDto>
{
    List<CardGroupDto> findAllByActif(boolean actif);

    List<CardGroupDto> findAllByCustomer(Long customerId);
}

