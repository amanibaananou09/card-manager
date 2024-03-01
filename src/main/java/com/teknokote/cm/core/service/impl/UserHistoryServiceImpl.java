package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.UserHistoryDao;
import com.teknokote.cm.core.service.UserHistoryService;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import com.teknokote.cm.dto.UserHistoryDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserHistoryServiceImpl extends GenericCheckedService<Long, UserHistoryDto> implements UserHistoryService
{
    @Autowired
    private ESSValidator<UserHistoryDto> validator;
    @Autowired
    private UserHistoryDao dao;
}
