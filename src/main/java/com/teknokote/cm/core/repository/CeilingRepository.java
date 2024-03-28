package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Ceiling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeilingRepository extends JpaRepository<Ceiling, Long>
{
}
