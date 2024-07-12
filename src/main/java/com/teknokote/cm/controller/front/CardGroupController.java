package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.CardGroupService;
import com.teknokote.cm.dto.CardGroupDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.CUSTOMER_CARD_GROUP_ROOT)
public class CardGroupController {
@Autowired
private CardGroupService cardGroupService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<CardGroupDto> addCardGroup(@PathVariable Long customerId,@RequestBody CardGroupDto dto) {
    CardGroupDto savedCardGroup = cardGroupService.createCardGroup(dto);
    return new ResponseEntity<>(savedCardGroup, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<CardGroupDto> updateCardGroup(@PathVariable Long customerId,@RequestBody CardGroupDto dto) {
        CardGroupDto savedCardGroup = cardGroupService.updateCardGroup(dto);
        return new ResponseEntity<>(savedCardGroup, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO_OLD)
    public ResponseEntity<CardGroupDto> getCardGroup(@PathVariable Long customerId,@PathVariable Long id)
    {
        CardGroupDto foundCardGroup = cardGroupService.cardGroupInformation(id);
        return new ResponseEntity<>(foundCardGroup, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.LIST_BY_ACTIF)
    public List<CardGroupDto> listCardGroupByActif(@PathVariable Long customerId,@PathVariable boolean actif) {
         return cardGroupService.findAllByActif(actif);
    }
    @GetMapping
    public List<CardGroupDto> listCardGroupByCustomer(@PathVariable Long customerId) {
        return cardGroupService.findAllByCustomer(customerId);
    }


    @PostMapping(EndPoints.DEACTIVATE)
    public ResponseEntity<CardGroupDto> deactivateCardGroup(@PathVariable Long customerId,@PathVariable Long id) {
        return ResponseEntity.ok(cardGroupService.deactivate(id));
    }

    @PostMapping(EndPoints.ACTIVATE)
    public ResponseEntity<CardGroupDto> activateCardGroup(@PathVariable Long customerId,@PathVariable Long id) {
        return ResponseEntity.ok(cardGroupService.activate(id));
    }
}
