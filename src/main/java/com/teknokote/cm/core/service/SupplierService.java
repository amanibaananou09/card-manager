package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;

public interface SupplierService extends ActivatableEntityService<Long, SupplierDto>, BaseService<Long, SupplierDto>
{
    SupplierDto findByReference(String reference);
}
