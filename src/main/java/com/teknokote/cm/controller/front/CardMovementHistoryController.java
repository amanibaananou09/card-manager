package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.interfaces.CardMovementHistoryService;
import com.teknokote.cm.dto.CardMovementHistoryDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.CUSTOMER_CARD_MOVEMENT)
public class CardMovementHistoryController {
    @Autowired
    private CardMovementHistoryService cardMovementHistoryService;

    @GetMapping
    public List<CardMovementHistoryDto> getCardHistory(@PathVariable Long customerId,@PathVariable Long id) {
        return cardMovementHistoryService.findByCustomerAndCardId(customerId,id);
    }
}
