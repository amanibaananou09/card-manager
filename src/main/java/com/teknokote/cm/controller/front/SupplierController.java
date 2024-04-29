package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.dto.SupplierDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.SUPPLIER_ROOT)
public class SupplierController {
@Autowired
private SupplierService supplierService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<SupplierDto> addSupplier(@RequestBody SupplierDto dto) {
    SupplierDto savedSupplier = supplierService.create(dto);
    return new ResponseEntity<>(savedSupplier, HttpStatus.CREATED);
    }

    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<SupplierDto> updateSupplier(@RequestBody SupplierDto dto) {
        SupplierDto savedSupplier = supplierService.update(dto);
        return new ResponseEntity<>(savedSupplier, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<SupplierDto> getSupplier(@PathVariable Long id)
    {
        SupplierDto foundSupplier = supplierService.checkedFindById(id);
        return new ResponseEntity<>(foundSupplier, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.REFERENCE)
    public ResponseEntity<SupplierDto> findSupplierWithReference(@PathVariable String reference)
    {
        SupplierDto foundSupplier = supplierService.findByReference(reference);
        return new ResponseEntity<>(foundSupplier, HttpStatus.CREATED);
    }
    @GetMapping
    public List<SupplierDto> listSupplierByActif(@RequestParam boolean actif) {
         return supplierService.findAllByActif(actif);
    }

    @PostMapping(EndPoints.DEACTIVATE)
    public ResponseEntity<SupplierDto> deactivateSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.deactivate(id));
    }

    @PostMapping(EndPoints.ACTIVATE)
    public ResponseEntity<SupplierDto> activateSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.activate(id));
    }
}
