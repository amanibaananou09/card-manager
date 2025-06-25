package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.service.interfaces.CustomerService;
import com.teknokote.cm.core.service.interfaces.UserService;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Getter
@Slf4j
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
        if (identifier == null) {
            log.warn("Null identifier provided to findCustomerByFilter");
            return List.of();
        }
        return getDao().findCustomerByIdentifier(identifier);
    }

    @Override
    public List<CustomerDto> findCustomerBySupplier(Long supplierId) {
        if (supplierId == null) {
            log.warn("Null supplierId provided to findCustomerBySupplier");
            return List.of();
        }
        return getDao().findCustomerBySupplier(supplierId);
    }

    @Transactional
    public CustomerDto addCustomer(CustomerDto dto) {
        Objects.requireNonNull(dto, "CustomerDto cannot be null");

        Set<UserDto> users = dto.getUsers();
        if (users != null && !users.isEmpty()) {
            users.forEach(userDto -> {
                Objects.requireNonNull(userDto, "UserDto in users set cannot be null");
                keycloakService.createUser(userDto);
            });
        }
        return create(dto, false);
    }

    @Transactional
    @Override
    public CustomerDto update(CustomerDto dto) {
        Objects.requireNonNull(dto, "CustomerDto cannot be null");

        CustomerDto customerDto = getDao().update(dto);

        userService.findByUsername(dto.getIdentifier())
                .ifPresentOrElse(
                        user -> {
                            if (dto.getUsers() != null && !dto.getUsers().isEmpty()) {
                                UserDto updatedUser = dto.getUsers().iterator().next();
                                keycloakService.updateUser(dto.getIdentifier(), updatedUser);
                            }
                        },
                        () -> log.warn("User with identifier {} not found for update.", dto.getIdentifier())
                );

        return customerDto;
    }

    @Override
    public List<String> generateIdentiferSuggestions(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            log.warn("Empty identifier provided for suggestions");
            return List.of();
        }
        return getDao().generateIdentiferSuggestions(identifier);
    }

    @Override
    public Optional<CustomerDto> findByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            log.warn("Null or blank identifier provided");
            return Optional.empty();
        }
        return getDao().findByIdentifier(identifier);
    }
}
