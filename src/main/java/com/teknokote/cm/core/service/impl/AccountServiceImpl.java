package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.AccountDao;
import com.teknokote.cm.core.service.AccountService;
import com.teknokote.cm.dto.AccountDto;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AccountServiceImpl extends ActivatableGenericCheckedService<Long, AccountDto> implements AccountService
{
    @Autowired
    private ESSValidator<AccountDto> validator;
    @Autowired
    private AccountDao dao;
}
