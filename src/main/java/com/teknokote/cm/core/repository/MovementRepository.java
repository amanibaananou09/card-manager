package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>
{
}
