package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.CardMovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardMovementHistoryRepository extends JpaRepository<CardMovementHistory,Long> {
    @Query("select cm from CardMovementHistory cm where cm.card.cardGroup.customerId = :customerId and cm.cardId = :cardId")
    List<CardMovementHistory> findByCustomerAndCardId(Long customerId, Long cardId);
}
