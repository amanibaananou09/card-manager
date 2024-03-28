package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.SalePointDao;
import com.teknokote.cm.core.service.SalePointService;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class SalePointServiceImpl extends GenericCheckedService<Long, SalePointDto> implements SalePointService
{
    @Autowired
    private ESSValidator<SalePointDto> validator;
    @Autowired
    private SalePointDao dao;
}
