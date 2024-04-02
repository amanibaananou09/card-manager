package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Card;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaActivatableRepository<Card, Long>
{
}
