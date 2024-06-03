package com.teknokote.cm.core.dao;

import com.teknokote.cm.core.repository.CustomerRepository;
import com.teknokote.cm.core.repository.SupplierRepository;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.dao.ActivatableDao;

import java.util.List;


public interface CustomerDao extends ActivatableDao<Long, CustomerDto> {
    List<CustomerDto> findCustomerByIdentifier(String identifier);

    List<CustomerDto> findCustomerBySupplier(Long supplierId);

    CustomerRepository getRepository();
}

