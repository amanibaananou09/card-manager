package com.teknokote.cm.core.service;

import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<Long, UserDto> {
    UserDao getDao();

    void updateLastConnection(String userName);

    List<UserDto> findBySupplier(Long supplierId);

    Optional<UserDto> findByUsername(String identifier);
}
