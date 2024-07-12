package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.CardGroup;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardGroupRepository extends JpaActivatableRepository<CardGroup, Long>
{
    List<CardGroup> findAllByActif(Boolean actif);
    @Query("select c from CardGroup c where c.customerId = :customerId order by c.audit.createdDate desc")
    List<CardGroup> findAllByCustomerId(Long customerId);

    CardGroup findAllByNameAndCustomerId(String name, Long customerId);
}
