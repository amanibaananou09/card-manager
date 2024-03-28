package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.AccountDao;
import com.teknokote.cm.core.dao.mappers.AccountMapper;
import com.teknokote.cm.core.model.Account;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.AccountRepository;
import com.teknokote.cm.dto.AccountDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class AccountDaoImpl extends JpaActivatableGenericDao<Long,User ,AccountDto, Account> implements AccountDao
{
    @Autowired
    private AccountMapper mapper;
    @Autowired
    private AccountRepository repository;
}
