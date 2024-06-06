package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.service.CustomerService;
import com.teknokote.cm.core.service.UserService;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Getter
public class CustomerServiceImpl extends ActivatableGenericCheckedService<Long, CustomerDto> implements CustomerService {
    @Autowired
    private ESSValidator<CustomerDto> validator;
    @Autowired
    private CustomerDao dao;
    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private UserService userService;

    public List<CustomerDto> findCustomerByFilter(String identifier) {
        List<CustomerDto> customerList = null;

        if (identifier != null) {
            customerList = getDao().findCustomerByIdentifier(identifier);
        } else {
            return null;
        }
        return customerList;
    }

    @Override
    public List<CustomerDto> findCustomerBySupplier(Long supplierId) {
        return getDao().findCustomerBySupplier(supplierId);
    }

    @Transactional
    public CustomerDto addCustomer(CustomerDto dto) {
        Set<UserDto> users = dto.getUsers();
        // Create users in Keycloak
        if (!users.isEmpty()) {
            for (UserDto userDto : users) {
                keycloakService.createUser(userDto);
            }
        }
        return create(dto, false);
    }
}
