package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CardGroupDao;
import com.teknokote.cm.core.service.CardGroupService;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CardGroupServiceImpl extends ActivatableGenericCheckedService<Long, CardGroupDto> implements CardGroupService
{
    @Autowired
    private ESSValidator<CardGroupDto> validator;
    @Autowired
    private CardGroupDao dao;
}
