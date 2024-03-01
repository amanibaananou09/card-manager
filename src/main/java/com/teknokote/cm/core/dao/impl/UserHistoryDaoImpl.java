package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.UserHistoryDao;
import com.teknokote.cm.core.dao.mappers.UserHistoryMapper;
import com.teknokote.cm.core.model.UserHistory;
import com.teknokote.cm.core.repository.UserHistoryRepository;
import com.teknokote.cm.dto.UserHistoryDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class UserHistoryDaoImpl extends JpaGenericDao<Long,UserHistoryDto, UserHistory> implements UserHistoryDao
{
    @Autowired
    private UserHistoryMapper mapper;
    @Autowired
    private UserHistoryRepository repository;
}
