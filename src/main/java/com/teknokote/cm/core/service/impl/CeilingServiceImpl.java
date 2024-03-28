package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CeilingDao;
import com.teknokote.cm.core.service.CeilingService;
import com.teknokote.cm.dto.CeilingDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CeilingServiceImpl extends GenericCheckedService<Long, CeilingDto> implements CeilingService
{
    @Autowired
    private ESSValidator<CeilingDto> validator;
    @Autowired
    private CeilingDao dao;
}
