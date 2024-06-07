package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Card;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaActivatableRepository<Card, Long> {
    @Query("select c from Card c where c.cardGroup.customerId = :customerId order by c.audit.createdDate desc")
    List<Card> findAllByCustomerId(Long customerId);

    List<Card> findAllByCardGroupId(Long cardGroupId);

    @Query("select c from Card c where c.cardGroup.customerId = :customerId and c.actif = :actif order by c.audit.createdDate desc")
    List<Card> findAllByActifAndCustomer(Boolean actif, Long customerId);

    @Query("SELECT ca FROM Card ca WHERE ca.cardGroup.customerId = :customerId " +
            "AND EXTRACT(MONTH FROM ca.expirationDate) = :month " +
            "AND EXTRACT(YEAR FROM ca.expirationDate) = :year " +
            "ORDER BY ca.audit.createdDate DESC")
    List<Card> findCardByMonthAndYear(int month, int year, Long customerId);

    @Query("SELECT ca FROM Card ca WHERE ca.cardGroup.customerId= :customerId and LOWER(ca.cardId) LIKE LOWER(CONCAT('%', :cardId, '%')) order by ca.audit.createdDate desc ")
    List<Card> findCardByCardId(String cardId, Long customerId);

    @Query("SELECT ca FROM Card ca WHERE ca.cardGroup.customerId= :customerId and LOWER(ca.holder) LIKE LOWER(CONCAT('%', :holder, '%')) order by ca.audit.createdDate desc ")
    List<Card> findCardByHolderName(String holder, Long customerId);

    Card findByTag(String tag);
}
