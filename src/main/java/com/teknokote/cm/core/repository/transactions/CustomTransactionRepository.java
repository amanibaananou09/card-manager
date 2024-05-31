package com.teknokote.cm.core.repository.transactions;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.dto.TransactionFilterDto;
import org.springframework.data.domain.Page;

public interface CustomTransactionRepository {
    Page<Transaction> findByCriteria(Long customerId, TransactionFilterDto filterDto, int page, int size);
}
