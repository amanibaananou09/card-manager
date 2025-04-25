package com.teknokote.cm.core.service.interfaces;

import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends ActivatableEntityService<Long, CustomerDto>, BaseService<Long, CustomerDto> {
    List<CustomerDto> findCustomerByFilter(String identifier);

    List<CustomerDto> findCustomerBySupplier(Long supplierId);

    CustomerDto addCustomer(CustomerDto dto);

    List<String> generateIdentiferSuggestions(String identifier);

    Optional<CustomerDto> findByIdentifier(String identifier);
}
