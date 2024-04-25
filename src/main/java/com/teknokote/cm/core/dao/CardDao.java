package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.dao.ActivatableDao;
import java.util.List;

public interface CardDao extends ActivatableDao<Long, CardDto>
{
    List<CardDto> findAllByCustomer(Long customerId);
}

