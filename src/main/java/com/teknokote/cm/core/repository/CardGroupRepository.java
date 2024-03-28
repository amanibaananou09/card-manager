package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.CardGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardGroupRepository extends JpaRepository<CardGroup, Long>
{
}
