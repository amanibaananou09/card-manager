package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Customer;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaActivatableRepository<Customer, Long> {
    @Query("SELECT ca FROM Customer ca WHERE LOWER(ca.identifier) LIKE LOWER(CONCAT('%', :identifier, '%')) order by ca.audit.createdDate desc")
    List<Customer> findCustomerByIdentifier(String identifier);

    List<Customer> findAllBySupplierId(Long supplierId);
}
