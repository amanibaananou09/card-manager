package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.core.repository.transactions.CustomTransactionRepository;
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
}
