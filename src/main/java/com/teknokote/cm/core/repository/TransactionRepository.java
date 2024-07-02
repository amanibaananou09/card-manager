package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.core.repository.transactions.CustomTransactionRepository;
import com.teknokote.cm.dto.DailyTransactionChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CustomTransactionRepository {
    @Query("SELECT t FROM Transaction t WHERE t.cardId = :cardId AND EXTRACT(MONTH FROM t.dateTime) = :month ORDER BY t.dateTime DESC LIMIT 1")
    Optional<Transaction> findLastTransactionByCardIdAndMonth(Long cardId, int month);

    @Query(" SELECT t FROM Transaction t WHERE t.cardId = :cardId and FUNCTION('DATE', t.dateTime) = CURRENT_DATE")
    List<Transaction> findAllByCardIdToday(Long cardId);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND t.dateTime BETWEEN :startDate AND :endDate " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId")
    List<DailyTransactionChart> findTransactionsBetweenDate(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND t.dateTime BETWEEN :startDate AND :endDate " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId")
    List<DailyTransactionChart> findTransactionsBetweenDateAndCardId(Long customerId, Long cardId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND FUNCTION('DATE', t.dateTime) = CURRENT_DATE " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId")
    List<DailyTransactionChart> findTodayTransactions(Long customerId);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "AND FUNCTION('DATE', t.dateTime) = CURRENT_DATE " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId")
    List<DailyTransactionChart> findTodayTransactionsWithCardId(Long customerId, Long cardId);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(WEEK FROM t.dateTime) = EXTRACT(WEEK FROM CURRENT_DATE) " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId " +
            "order by CASE WHEN to_char(t.dateTime, 'DY') = 'SUN' THEN 1 ELSE 0 END, MIN(t.dateTime) ASC")
    List<DailyTransactionChart> findWeeklyTransactions(Long customerId);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'DY'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(WEEK FROM t.dateTime) = EXTRACT(WEEK FROM CURRENT_DATE) " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'DY'), t.product.name, t.card.cardId " +
            "order by CASE WHEN to_char(t.dateTime, 'DY') = 'SUN' THEN 1 ELSE 0 END, MIN(t.dateTime) ASC")
    List<DailyTransactionChart> findWeeklyTransactionsWithCardId(Long customerId, Long cardId);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(MONTH FROM t.dateTime) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "GROUP BY TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.cardId")
    List<DailyTransactionChart> findMonthlyTransactions(Long customerId);

    @Query("SELECT new com.teknokote.cm.dto.DailyTransactionChart(TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.cardId, SUM(t.quantity)) " +
            "FROM Transaction t join t.card c where c.cardGroup.customer.id = :customerId " +
            "and EXTRACT(MONTH FROM t.dateTime) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND t.cardId = :cardId " +
            "GROUP BY TO_CHAR(t.dateTime, 'dd'),t.product.name, t.card.cardId")
    List<DailyTransactionChart> findMonthlyTransactionsWithCardId(Long customerId, Long cardId);
}
