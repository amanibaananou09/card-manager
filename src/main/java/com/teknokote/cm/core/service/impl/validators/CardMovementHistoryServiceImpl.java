package com.teknokote.cm.core.service.impl.validators;

import com.teknokote.cm.core.dao.CardMovementHistoryDao;
import com.teknokote.cm.core.service.interfaces.CardMovementHistoryService;
import com.teknokote.cm.core.service.interfaces.CardService;
import com.teknokote.cm.dto.CardMovementHistoryDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class CardMovementHistoryServiceImpl extends GenericCheckedService<Long, CardMovementHistoryDto>  implements CardMovementHistoryService {
    @Autowired
    private ESSValidator<CardMovementHistoryDto> validator;
    @Autowired
    private CardMovementHistoryDao dao;
    @Autowired
    private CardService cardService;

    @Override
    public List<CardMovementHistoryDto> findByCustomerAndCardId(Long customerId, Long id) {
        return dao.findByCustomerAndCardId(customerId,id);
    }
}
