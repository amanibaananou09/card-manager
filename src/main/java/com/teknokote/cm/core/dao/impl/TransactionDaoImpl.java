package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.dao.mappers.TransactionMapper;
import com.teknokote.cm.core.model.*;
import com.teknokote.cm.core.repository.TransactionRepository;
import com.teknokote.cm.dto.DailyTransactionChart;
import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.cm.dto.TransactionFilterDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Getter
@Setter
public class TransactionDaoImpl extends JpaGenericDao<Long, TransactionDto, Transaction> implements TransactionDao {
    @Autowired
    private TransactionMapper mapper;
    @Autowired
    private TransactionRepository repository;

    @Override
    protected Transaction beforeCreate(Transaction transaction, TransactionDto dto) {
        if (Objects.nonNull(dto.getCardId())) {
            transaction.setCard(getEntityManager().getReference(Card.class, dto.getCardId()));
        }
        if (Objects.nonNull(dto.getAuthorizationId())) {
            transaction.setAuthorization(getEntityManager().getReference(Authorization.class, dto.getAuthorizationId()));
        }
        if (Objects.nonNull(dto.getProductId())) {
            transaction.setProduct(getEntityManager().getReference(Product.class, dto.getProductId()));
        }
        if (Objects.nonNull(dto.getSalePointId())) {
            transaction.setSalePoint(getEntityManager().getReference(SalePoint.class, dto.getSalePointId()));
        }
        return super.beforeCreate(transaction, dto);
    }

    @Override
    protected Transaction beforeUpdate(Transaction transaction, TransactionDto dto) {
        if (Objects.nonNull(dto.getCardId())) {
            transaction.setCard(getEntityManager().getReference(Card.class, dto.getCardId()));
        }
        if (Objects.nonNull(dto.getAuthorizationId())) {
            transaction.setAuthorization(getEntityManager().getReference(Authorization.class, dto.getAuthorizationId()));
        }
        if (Objects.nonNull(dto.getProductId())) {
            transaction.setProduct(getEntityManager().getReference(Product.class, dto.getProductId()));
        }
        if (Objects.nonNull(dto.getSalePointId())) {
            transaction.setSalePoint(getEntityManager().getReference(SalePoint.class, dto.getSalePointId()));
        }
        return super.beforeUpdate(transaction, dto);
    }

    @Override
    public Optional<TransactionDto> findLastTransactionByCardIdAndMonth(Long cardId, int month) {
        return Optional.ofNullable(getMapper().toDto(getRepository().findLastTransactionByCardIdAndMonth(cardId, month).orElse(null)));

    }

    @Override
    public List<TransactionDto> findTodayTransaction(Long cardId) {
        return getRepository().findAllByCardIdToday(cardId).stream().map(getMapper()::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DailyTransactionChart> todayChartTransaction(Long customerId) {
        return getRepository().findTodayTransactions(customerId);
    }

    @Override
    public List<DailyTransactionChart> todayChartTransactionWithCardId(Long customerId, Long cardId) {
        return getRepository().findTodayTransactionsWithCardId(customerId, cardId);
    }

    @Override
    public List<DailyTransactionChart> weeklyChartTransaction(Long customerId) {
        return getRepository().findWeeklyTransactions(customerId);
    }

    @Override
    public List<DailyTransactionChart> weeklyChartTransactionWithCardId(Long customerId, Long cardId) {
        return getRepository().findWeeklyTransactionsWithCardId(customerId, cardId);
    }

    @Override
    public List<DailyTransactionChart> monthlyChartTransaction(Long customerId) {
        return getRepository().findMonthlyTransactions(customerId);
    }

    @Override
    public List<DailyTransactionChart> monthlyChartTransactionWithCardId(Long customerId, Long cardId) {
        return getRepository().findMonthlyTransactionsWithCardId(customerId, cardId);
    }

    @Override
    public List<DailyTransactionChart> findTransactionBetween(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return getRepository().findTransactionsBetweenDate(customerId, startDate, endDate);
    }

    @Override
    public List<DailyTransactionChart> findTransactionBetweenDateWithCardId(Long customerId, Long cardId, LocalDateTime startDate, LocalDateTime endDate) {
        return getRepository().findTransactionsBetweenDateAndCardId(customerId, cardId, startDate, endDate);
    }

    @Override
    public Page<Transaction> findByCriteria(Long customerId, TransactionFilterDto filterDto, int page, int size) {
        return repository.findByCriteria(customerId, filterDto, page, size);
    }

}
