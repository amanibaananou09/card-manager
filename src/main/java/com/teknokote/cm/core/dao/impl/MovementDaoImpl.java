package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.MovementDao;
import com.teknokote.cm.core.dao.mappers.MovementMapper;
import com.teknokote.cm.core.model.Movement;
import com.teknokote.cm.core.repository.MovementRepository;
import com.teknokote.cm.dto.MovementDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class MovementDaoImpl extends JpaGenericDao<Long, MovementDto, Movement> implements MovementDao
{
   @Autowired
   private MovementMapper mapper;
   @Autowired
   private MovementRepository repository;
}
