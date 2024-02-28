package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.MovementDao;
import com.teknokote.cm.core.service.MovementService;
import com.teknokote.cm.dto.MovementDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class MovementServiceImpl extends GenericCheckedService<Long, MovementDto> implements MovementService
{
   @Autowired
   private ESSValidator<MovementDto> validator;
   @Autowired
   private MovementDao dao;
}
