package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Supplier;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaActivatableRepository<Supplier, Long> {

    Supplier findByReference(String reference);
    Supplier findByReferenceAndIdentifier(String reference, String identifier);
    @Query("select sp from Supplier sp join sp.users user where user.username = :username")
    Optional<Supplier> findSupplierByUser(String username);
}

