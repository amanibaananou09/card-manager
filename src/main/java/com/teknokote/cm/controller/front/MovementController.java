package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.MovementService;
import com.teknokote.cm.dto.MovementDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.MOVEMENT_ROOT)
public class MovementController {
@Autowired
private MovementService movementService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<MovementDto> addMovement(@RequestBody MovementDto dto) {
    MovementDto savedMovement = movementService.create(dto);
    return new ResponseEntity<>(savedMovement, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<MovementDto> updateMovement(@RequestBody MovementDto dto) {
        MovementDto savedMovement = movementService.update(dto);
        return new ResponseEntity<>(savedMovement, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<MovementDto> getMovement(@PathVariable Long id)
    {
        MovementDto foundMovement = movementService.checkedFindById(id);
        return new ResponseEntity<>(foundMovement, HttpStatus.CREATED);
    }
    @GetMapping
    public List<MovementDto> listMovement() {
        return movementService.findAll();
    }
}
