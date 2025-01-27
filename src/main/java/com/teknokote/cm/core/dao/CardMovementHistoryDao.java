package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.CardMovementHistoryDto;
import com.teknokote.core.dao.BasicDao;

import java.util.List;

public interface CardMovementHistoryDao extends BasicDao<Long, CardMovementHistoryDto> {
    List<CardMovementHistoryDto> findByCustomerAndCardId(Long customerId, Long id);
}
