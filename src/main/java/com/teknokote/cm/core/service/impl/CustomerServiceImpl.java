package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.service.CustomerService;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CustomerServiceImpl extends GenericCheckedService<Long, CustomerDto> implements CustomerService
{
   @Autowired
   private ESSValidator<CustomerDto> validator;
   @Autowired
   private CustomerDao dao;
}
