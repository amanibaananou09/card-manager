package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.repository.UserRepository;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.dao.BasicDao;

import java.time.LocalDateTime;


public interface UserDao extends BasicDao<Long, UserDto>
{
   UserRepository getRepository();
   void updateLastConnection(String userName, LocalDateTime connectionDate);
}

