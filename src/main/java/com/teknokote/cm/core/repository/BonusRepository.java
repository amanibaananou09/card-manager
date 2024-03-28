package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long>
{
}
