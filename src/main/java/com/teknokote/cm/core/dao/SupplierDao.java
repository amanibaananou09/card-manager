package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.dao.ActivatableDao;


public interface SupplierDao extends ActivatableDao<Long, SupplierDto>
{
    SupplierDto findAllByReference(String reference);
}

