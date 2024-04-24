package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.dao.ActivatableDao;

import java.util.List;


public interface SupplierDao extends ActivatableDao<Long, SupplierDto>
{
    SupplierDto findAllByReference(String reference);

    SupplierDto findAllByReferenceAndName(String reference,String name);
}

