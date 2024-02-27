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
@RequestMapping(EndPoints.CARD_ROOT)
public class CardController {
@Autowired
private CardService cardService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<CardDto> addCard(@RequestBody CardDto dto) {
    CardDto savedCard = cardService.create(dto);
    return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<CardDto> updateCard(@RequestBody CardDto dto) {
        CardDto savedCard = cardService.update(dto);
        return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<CardDto> getCard(@PathVariable Long id)
    {
        CardDto foundCard = cardService.checkedFindById(id);
        return new ResponseEntity<>(foundCard, HttpStatus.CREATED);
    }
    @GetMapping
    public List<CardDto> listCard() {
        return cardService.findAll();
    }
}
