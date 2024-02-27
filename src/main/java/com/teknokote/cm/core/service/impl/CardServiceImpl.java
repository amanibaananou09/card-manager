package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.CardDao;
import com.teknokote.cm.core.service.CardService;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import com.teknokote.cm.dto.CardDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CardServiceImpl extends GenericCheckedService<Long, CardDto> implements CardService
{
    @Autowired
    private ESSValidator<CardDto> validator;
    @Autowired
    private CardDao dao;
}
