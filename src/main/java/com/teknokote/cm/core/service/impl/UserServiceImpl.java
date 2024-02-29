package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.service.UserService;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Getter
public class UserServiceImpl extends GenericCheckedService<Long, UserDto> implements UserService
{
   @Autowired
   private ESSValidator<UserDto> validator;
   @Autowired
   private UserDao dao;

   @Transactional
   public void updateLastConnection(String userName) {
      getDao().updateLastConnection(userName, LocalDateTime.now());
   }
}
