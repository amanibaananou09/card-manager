package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.dao.mappers.CustomerMapper;
import com.teknokote.cm.core.model.Customer;
import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.CustomerRepository;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Getter
@Setter
public class CustomerDaoImpl extends JpaActivatableGenericDao<Long, User, CustomerDto, Customer> implements CustomerDao {
    @Autowired
    private CustomerMapper mapper;
    @Autowired
    private CustomerRepository repository;

    @Override
    protected Customer beforeCreate(Customer customer, CustomerDto dto) {
        customer.setActif(true);
        customer.setDateStatusChange(LocalDateTime.now());
        customer.setSupplier(getEntityManager().getReference(Supplier.class, dto.getSupplierId()));

        Customer savedCustomer = super.beforeCreate(customer, dto);

        if (!dto.getAccounts().isEmpty()) {
            savedCustomer.getAccounts().forEach(account -> {
                        account.setCustomer(savedCustomer);
                        account.setDateStatusChange(LocalDateTime.now());
                        account.setActif(true);
                        if (!account.getMovements().isEmpty()) {
                            account.getMovements().forEach(movement -> movement.setAccount(account));
                        }
                    }
            );
        }
        return savedCustomer;
    }

    @Override
    protected Customer beforeUpdate(Customer customer, CustomerDto dto) {

        Customer savedCustomer = super.beforeUpdate(customer, dto);

        if (!dto.getAccounts().isEmpty()) {
            savedCustomer.getAccounts().forEach(account -> {
                        account.setCustomer(savedCustomer);
                        account.setDateStatusChange(LocalDateTime.now());
                        account.setActif(true);
                if (!account.getMovements().isEmpty()) {
                    account.getMovements().forEach(movement -> movement.setAccount(account));
                }
                    }
            );
        }
        return savedCustomer;
    }


    public List<CustomerDto> findCustomerByIdentifier(String identifier) {
        return getRepository().findCustomerByIdentifier(identifier).stream().map(getMapper()::toDto).toList();
    }

    @Override
    public List<CustomerDto> findCustomerBySupplier(Long supplierId) {
        return getRepository().findAllBySupplierId(supplierId).stream().map(getMapper()::toDto).toList();
    }
}
