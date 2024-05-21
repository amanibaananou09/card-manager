package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.core.service.BaseService;

public interface TransactionService extends BaseService<Long, TransactionDto>
{

    TransactionDto createTransaction(TransactionDto dto);
}
