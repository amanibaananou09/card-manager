package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.dao.mappers.CustomerMapper;
import com.teknokote.cm.core.model.Customer;
import com.teknokote.cm.core.repository.CustomerRepository;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class CustomerDaoImpl extends JpaGenericDao<Long, CustomerDto, Customer> implements CustomerDao
{
   @Autowired
   private CustomerMapper mapper;
   @Autowired
   private CustomerRepository repository;
}
