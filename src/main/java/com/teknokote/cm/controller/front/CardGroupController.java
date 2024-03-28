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
@RequestMapping(EndPoints.CARD_GROUP_ROOT)
public class CardGroupController {
@Autowired
private CardGroupService cardGroupService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<CardGroupDto> addCardGroup(@RequestBody CardGroupDto dto) {
    CardGroupDto savedCardGroup = cardGroupService.create(dto);
    return new ResponseEntity<>(savedCardGroup, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<CardGroupDto> updateCardGroup(@RequestBody CardGroupDto dto) {
        CardGroupDto savedCardGroup = cardGroupService.update(dto);
        return new ResponseEntity<>(savedCardGroup, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<CardGroupDto> getCardGroup(@PathVariable Long id)
    {
        CardGroupDto foundCardGroup = cardGroupService.checkedFindById(id);
        return new ResponseEntity<>(foundCardGroup, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.LIST_BY_ACTIF)
    public List<CardGroupDto> listCardGroupByActif(@PathVariable boolean actif) {
         return cardGroupService.findAllByActif(actif);
    }


    @PostMapping(EndPoints.DEACTIVATE)
    public ResponseEntity<CardGroupDto> deactivateCardGroup(@PathVariable Long id) {
        return ResponseEntity.ok(cardGroupService.deactivate(id));
    }

    @PostMapping(EndPoints.ACTIVATE)
    public ResponseEntity<CardGroupDto> activateCardGroup(@PathVariable Long id) {
        return ResponseEntity.ok(cardGroupService.activate(id));
    }
}
