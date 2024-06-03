package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.cm.dto.TransactionFilterDto;
import com.teknokote.core.dao.BasicDao;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TransactionDao extends BasicDao<Long, TransactionDto>
{
    Optional<TransactionDto> findLastTransactionByCardIdAndMonth(Long cardId, int month);

    List<TransactionDto> findTodayTransaction(Long cardId, LocalDateTime dateTime);

    Page<Transaction> findByCriteria(Long customerId, TransactionFilterDto filterDto, int page, int size);
}

