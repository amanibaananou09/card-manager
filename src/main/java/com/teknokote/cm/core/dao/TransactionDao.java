package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.core.dao.BasicDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TransactionDao extends BasicDao<Long, TransactionDto>
{
    Optional<TransactionDto> findLastTransactionByCardIdAndMonth(Long cardId, int month);

    List<TransactionDto> findTodayTransaction(Long cardId, LocalDateTime dateTime);
}

