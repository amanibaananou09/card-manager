package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CustomerDao;
import com.teknokote.cm.core.service.CustomerService;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CustomerServiceImpl extends ActivatableGenericCheckedService<Long, CustomerDto> implements CustomerService
{
    @Autowired
    private ESSValidator<CustomerDto> validator;
    @Autowired
    private CustomerDao dao;
}
