package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.repository.SupplierRepository;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.dao.ActivatableDao;


public interface SupplierDao extends ActivatableDao<Long, SupplierDto>
{
    SupplierRepository getRepository();

    SupplierDto findAllByReference(String reference);

    SupplierDto findAllByReferenceAndIdentifier(String reference, String identifier);
}

