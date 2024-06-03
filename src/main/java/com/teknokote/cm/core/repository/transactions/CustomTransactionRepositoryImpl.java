package com.teknokote.cm.core.repository.transactions;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.dto.TransactionFilterDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Transaction> findByCriteria(Long customerId, TransactionFilterDto filterDto, int page, int size) {
        return FindTransactionsQueryBuilder.init(entityManager, PageRequest.of(page, size))
                .addCustomerIdClause(customerId)
                .addCardClause(filterDto.getCardIds())
                .addSalePointClause(filterDto.getSalePointIds())
                .addProductClause(filterDto.getProductIds())
                .addDateTimeStartBetweenClause(filterDto.getPeriod())
                .addCityClause(filterDto.getCity())
                .getResults();
    }
}
