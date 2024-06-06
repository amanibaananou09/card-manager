package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.repository.UserRepository;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.dao.BasicDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface UserDao extends BasicDao<Long, UserDto> {
    UserRepository getRepository();

    void updateLastConnection(String userName, LocalDateTime connectionDate);

    Optional<UserDto> findAllByUsername(String name);

    List<String> generateUsernameSuggestions(String baseUsername);
}

