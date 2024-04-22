package com.teknokote.cm.core.service.impl;
import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.service.UserService;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Getter
public class UserServiceImpl extends GenericCheckedService<Long, UserDto> implements UserService
{
   @Autowired
   private ESSValidator<UserDto> validator;
   @Autowired
   private UserDao dao;
   @Autowired
   private SupplierDao supplierDao;

   @Transactional
   public void updateLastConnection(String userName) {
      getDao().updateLastConnection(userName, LocalDateTime.now());
   }

   @Override
   public List<UserDto> findBySupplier(Long supplierId) {
      Optional<SupplierDto> supplier = supplierDao.findById(supplierId);
      List<UserDto> users = new ArrayList<>();
      if (supplier.isPresent()) {
         Set<UserDto> usersSet = supplier.get().getUsers();
         users.addAll(usersSet);
      }
      return users;
   }
}
