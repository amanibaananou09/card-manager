package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.core.service.BaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService extends BaseService<Long, TransactionDto>
{

    TransactionDto createTransaction(TransactionDto dto);

    Optional<TransactionDto> findLastTransactionByCardIdAndMonth(Long id, int monthValue);

    List<TransactionDto> findTodayTransaction(Long cardId , LocalDateTime dateTime);
}
