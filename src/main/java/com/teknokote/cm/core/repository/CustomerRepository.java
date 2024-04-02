package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Customer;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaActivatableRepository<Customer, Long>
{
}
