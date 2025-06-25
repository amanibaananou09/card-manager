package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.SalePointDao;
import com.teknokote.cm.core.service.interfaces.SalePointService;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class SalePointServiceImpl extends GenericCheckedService<Long, SalePointDto> implements SalePointService
{
    @Autowired
    private ESSValidator<SalePointDto> validator;
    @Autowired
    private SalePointDao dao;

    @Override
    public List<SalePointDto> findBySupplier(Long supplierId) {
        return getDao().findBySupplier(supplierId);
    }
}
