package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.BonusDao;
import com.teknokote.cm.core.service.interfaces.BonusService;
import com.teknokote.cm.dto.BonusDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class BonusServiceImpl extends GenericCheckedService<Long, BonusDto> implements BonusService
{
    @Autowired
    private ESSValidator<BonusDto> validator;
    @Autowired
    private BonusDao dao;
}
