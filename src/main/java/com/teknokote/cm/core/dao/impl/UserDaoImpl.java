package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.dao.mappers.UserMapper;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.UserRepository;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@Getter
@Setter
public class UserDaoImpl extends JpaGenericDao<Long, UserDto, User> implements UserDao
{
   @Autowired
   private UserMapper mapper;
   @Autowired
   private UserRepository repository;

   public void updateLastConnection(String userName, LocalDateTime connectionDate) {
      getRepository().updateLastConnection(userName, connectionDate);
   }
}
