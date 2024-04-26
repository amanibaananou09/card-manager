package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;

import java.util.List;

public interface CustomerService extends ActivatableEntityService<Long, CustomerDto>, BaseService<Long, CustomerDto> {
    List<CustomerDto> findCustomerByFilter(String identifier);
    List<CustomerDto> findCustomerBySupplier(Long supplierId);
}
