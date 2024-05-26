package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, Long>
{
    @Query("SELECT a FROM Authorization a WHERE a.dateTime = (SELECT MAX(a2.dateTime) FROM Authorization a2)")
    Authorization findLatestAuthorization();

    Authorization findByReference(String authorizationReference);
}
