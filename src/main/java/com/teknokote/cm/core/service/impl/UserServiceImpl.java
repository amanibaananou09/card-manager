package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.service.UserService;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import com.teknokote.cm.dto.UserDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserServiceImpl extends GenericCheckedService<Long, UserDto> implements UserService
{
    @Autowired
    private ESSValidator<UserDto> validator;
    @Autowired
    private UserDao dao;
}
