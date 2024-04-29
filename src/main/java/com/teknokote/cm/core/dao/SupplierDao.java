package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.repository.SupplierRepository;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.dao.ActivatableDao;

import java.util.List;


public interface SupplierDao extends ActivatableDao<Long, SupplierDto>
{
    SupplierRepository getRepository();

    SupplierDto findAllByReference(String reference);

    SupplierDto findAllByReferenceAndName(String reference,String name);
}

