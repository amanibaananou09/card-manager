package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Supplier;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaActivatableRepository<Supplier, Long>
{
}
