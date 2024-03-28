package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.SalePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalePointRepository extends JpaRepository<SalePoint, Long>
{
}
