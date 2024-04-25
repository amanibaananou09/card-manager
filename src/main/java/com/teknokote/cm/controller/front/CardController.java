package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.CardService;
import com.teknokote.cm.dto.CardDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.CUSTOMER_CARD_ROOT)
public class CardController {
@Autowired
private CardService cardService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<CardDto> addCard(@PathVariable Long customerId,@RequestBody CardDto dto) {
    CardDto savedCard = cardService.create(dto);
    return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }

    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<CardDto> updateCard(@PathVariable Long customerId,@RequestBody CardDto dto) {
        CardDto savedCard = cardService.update(dto);
        return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<CardDto> getCard(@PathVariable Long customerId,@PathVariable Long id)
    {
        CardDto foundCard = cardService.checkedFindById(id);
        return new ResponseEntity<>(foundCard, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.LIST_BY_ACTIF)
    public List<CardDto> listCardByActif(@PathVariable Long customerId,@PathVariable boolean actif) {
         return cardService.findAllByActif(actif);
    }
    @GetMapping
    public List<CardDto> listCardByCustomer(@PathVariable Long customerId) {
        return cardService.findAllByCustomer(customerId);
    }

    @PostMapping(EndPoints.DEACTIVATE)
    public ResponseEntity<CardDto> deactivateCard(@PathVariable Long customerId,@PathVariable Long id) {
        return ResponseEntity.ok(cardService.deactivate(id));
    }

    @PostMapping(EndPoints.ACTIVATE)
    public ResponseEntity<CardDto> activateCard(@PathVariable Long customerId,@PathVariable Long id) {
        return ResponseEntity.ok(cardService.activate(id));
    }
}
