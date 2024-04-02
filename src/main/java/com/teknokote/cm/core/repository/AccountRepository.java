package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Account;
import com.teknokote.core.dao.JpaActivatableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaActivatableRepository<Account, Long>
{
}
