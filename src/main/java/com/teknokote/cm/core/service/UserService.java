package com.teknokote.cm.core.service;

import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.dao.BasicDao;
import com.teknokote.core.service.BaseService;

public interface UserService extends BaseService<Long, UserDto>
{
   UserDao getDao();
   void updateLastConnection(String userName);
}
