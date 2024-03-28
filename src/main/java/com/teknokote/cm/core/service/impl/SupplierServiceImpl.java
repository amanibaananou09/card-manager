package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class SupplierServiceImpl extends ActivatableGenericCheckedService<Long, SupplierDto> implements SupplierService
{
    @Autowired
    private ESSValidator<SupplierDto> validator;
    @Autowired
    private SupplierDao dao;
}
