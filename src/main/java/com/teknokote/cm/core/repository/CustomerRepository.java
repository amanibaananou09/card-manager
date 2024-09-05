package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Customer;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaActivatableRepository<Customer, Long> {
    @Query("SELECT ca FROM Customer ca WHERE LOWER(ca.identifier) LIKE LOWER(CONCAT('%', :identifier, '%')) order by ca.audit.createdDate asc")
    List<Customer> findCustomerByIdentifier(String identifier);

    @Query("select c from Customer c where c.supplaierId = :supplierId order by c.audit.createdDate asc ")
    List<Customer> findAllBySupplierId(Long supplierId);

    @Query("select sp from Customer sp join sp.users user where user.username = :username")
    Optional<Customer> findCustomerByUser(String username);

    boolean existsByIdentifier(String identifier);

    Optional<Customer> findAllByIdentifierIgnoreCase(String identifier);
}
