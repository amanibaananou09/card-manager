package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.CeilingService;
import com.teknokote.cm.dto.CeilingDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.CEILING_ROOT)
public class CeilingController {
@Autowired
private CeilingService ceilingService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<CeilingDto> addCeiling(@RequestBody CeilingDto dto) {
    CeilingDto savedCeiling = ceilingService.create(dto);
    return new ResponseEntity<>(savedCeiling, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<CeilingDto> updateCeiling(@RequestBody CeilingDto dto) {
        CeilingDto savedCeiling = ceilingService.update(dto);
        return new ResponseEntity<>(savedCeiling, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<CeilingDto> getCeiling(@PathVariable Long id)
    {
        CeilingDto foundCeiling = ceilingService.checkedFindById(id);
        return new ResponseEntity<>(foundCeiling, HttpStatus.CREATED);
    }
    @GetMapping
    public List<CeilingDto> listCeiling() {
        return ceilingService.findAll();
    }
}
