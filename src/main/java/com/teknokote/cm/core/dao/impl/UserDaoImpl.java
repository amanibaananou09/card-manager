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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Getter
@Setter
public class UserDaoImpl extends JpaGenericDao<Long, UserDto, User> implements UserDao {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private UserRepository repository;

    @Override
    protected User beforeCreate(User user, UserDto dto) {
        user.setActif(true);
        user.setDateStatusChange(LocalDateTime.now());
        return super.beforeCreate(user, dto);
    }

    public void updateLastConnection(String userName, LocalDateTime connectionDate) {
        getRepository().updateLastConnection(userName, connectionDate);
    }

    @Override
    public Optional<UserDto> findAllByUsername(String name) {
        return getRepository().findAllByUsernameIgnoreCase(name).map(getMapper()::toDto);
    }

    public List<String> generateUsernameSuggestions(String baseUsername) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            String suggestedUsername = baseUsername + String.format("%02d", i);
            if (!getRepository().existsByUsername(suggestedUsername)) {
                suggestions.add(suggestedUsername);
            }
        }
        return suggestions;
    }


}
