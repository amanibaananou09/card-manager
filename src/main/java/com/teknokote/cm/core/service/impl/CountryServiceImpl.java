package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CountryDao;
import com.teknokote.cm.core.service.interfaces.CountryService;
import com.teknokote.cm.dto.CountryDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CountryServiceImpl extends GenericCheckedService<Long, CountryDto> implements CountryService
{
    @Autowired
    private ESSValidator<CountryDto> validator;
    @Autowired
    private CountryDao dao;
}
