package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, Long>
{
}
