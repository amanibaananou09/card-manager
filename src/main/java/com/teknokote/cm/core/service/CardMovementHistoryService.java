package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.CardMovementHistoryDto;
import com.teknokote.core.service.BaseService;

import java.util.List;

public interface CardMovementHistoryService extends BaseService<Long, CardMovementHistoryDto> {
    List<CardMovementHistoryDto> findByCustomerAndCardId(Long customerId, Long id);
}
