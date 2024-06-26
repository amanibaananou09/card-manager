package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.core.repository.transactions.CustomTransactionRepository;
import com.teknokote.cm.dto.TransactionChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> , CustomTransactionRepository
{
    @Query("SELECT t FROM Transaction t WHERE t.cardId = :cardId AND EXTRACT(MONTH FROM t.dateTime) = :month ORDER BY t.dateTime DESC LIMIT 1")
    Optional<Transaction> findLastTransactionByCardIdAndMonth(Long cardId, int month);

    List<Transaction> findAllByCardIdAndDateTimeBefore(Long cardId, LocalDateTime dateTime);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND t.dateTime BETWEEN :startDate AND :endDate " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id")
    List<TransactionChart> findTransactionsBetweenDate(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND t.dateTime BETWEEN :startDate AND :endDate " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id")
    List<TransactionChart> findTransactionsBetweenDateAndCardId(Long customerId,Long cardId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND FUNCTION('DATE', t.dateTime) = CURRENT_DATE " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id")
    List<TransactionChart> findTodayTransactions(Long customerId);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND FUNCTION('DATE', t.dateTime) = CURRENT_DATE " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id")
    List<TransactionChart> findTodayTransactionsWithCardId(Long customerId,Long cardId);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(WEEK FROM t.dateTime) = EXTRACT(WEEK FROM CURRENT_DATE) " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id")
    List<TransactionChart> findWeeklyTransactions(Long customerId);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(WEEK FROM t.dateTime) = EXTRACT(WEEK FROM CURRENT_DATE) " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.id")
    List<TransactionChart> findWeeklyTransactionsWithCardId(Long customerId,Long cardId);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(MONTH FROM t.dateTime) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "GROUP BY TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.id")
    List<TransactionChart> findMonthlyTransactions(Long customerId);

    @Query("SELECT new com.teknokote.cm.dto.TransactionChart(TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.id, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(MONTH FROM t.dateTime) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.id")
    List<TransactionChart> findMonthlyTransactionsWithCardId(Long customerId,Long cardId);
}
