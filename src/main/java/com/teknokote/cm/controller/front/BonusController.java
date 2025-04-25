package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.interfaces.BonusService;
import com.teknokote.cm.dto.BonusDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.BONUS_ROOT)
public class BonusController {
@Autowired
private BonusService bonusService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<BonusDto> addBonus(@RequestBody BonusDto dto) {
    BonusDto savedBonus = bonusService.create(dto);
    return new ResponseEntity<>(savedBonus, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<BonusDto> updateBonus(@RequestBody BonusDto dto) {
        BonusDto savedBonus = bonusService.update(dto);
        return new ResponseEntity<>(savedBonus, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<BonusDto> getBonus(@PathVariable Long id)
    {
        BonusDto foundBonus = bonusService.checkedFindById(id);
        return new ResponseEntity<>(foundBonus, HttpStatus.CREATED);
    }
    @GetMapping
    public List<BonusDto> listBonus() {
        return bonusService.findAll();
    }
}
