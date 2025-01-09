package com.teknokote.cm.core.service;

import com.teknokote.cm.core.model.EnumCeilingLimitType;
import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.dto.DailyTransactionChart;
import com.teknokote.cm.dto.TransactionChart;
import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.cm.dto.TransactionFilterDto;
import com.teknokote.core.service.BaseService;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService extends BaseService<Long, TransactionDto> {

    TransactionDto createTransaction(TransactionDto dto);
    Optional<TransactionDto> findLastTransactionByCardId(Long id, EnumCeilingLimitType limitType, LocalDateTime dateTime);
    Page<Transaction> findTransactionsByFilter(Long customerId, TransactionFilterDto filterDto, int page, int size);
    TransactionDto mapToTransactionDto(Transaction transaction);
    List<DailyTransactionChart> chartTransaction(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate);
    List<TransactionChart> getTransactionChart(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate);
    List<DailyTransactionChart> getDailyTransactionChart(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate);
}
