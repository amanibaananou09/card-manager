package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.CardGroup;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardGroupRepository extends JpaActivatableRepository<CardGroup, Long>
{
    List<CardGroup> findAllByActif(Boolean actif);
}
