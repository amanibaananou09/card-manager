package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Card;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaActivatableRepository<Card, Long>
{
    @Query("select c from Card c where c.account.customerId = :customerId")
    List<Card> findAllByCustomerId(Long customerId);
}
