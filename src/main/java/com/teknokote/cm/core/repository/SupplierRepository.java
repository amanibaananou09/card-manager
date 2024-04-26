package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Supplier;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaActivatableRepository<Supplier, Long> {
    @Query("select sp from Supplier sp WHERE sp.reference = :reference")
    Supplier findAllByReference(@Param("reference") String reference);

    @Query("select sp from Supplier sp WHERE sp.reference = :reference and sp.name = :name")
    Supplier findByReferenceAndName(@Param("reference") String reference, @Param("name") String name);
    @Query("select sp from Supplier sp join sp.users user where user.username = :username")
    Optional<Supplier> findSupplierByUser(String username);
}

