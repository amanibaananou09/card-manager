package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.SalePointService;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.dto.SalePointDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.SALE_POINT_ROOT)
public class SalePointController {
@Autowired
private SalePointService salePointService;
@Autowired
private SupplierService supplierService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<SalePointDto> addSalePoint(@RequestBody SalePointDto dto) {
    SalePointDto savedSalePoint = supplierService.createSalePoint(dto);
    return new ResponseEntity<>(savedSalePoint, HttpStatus.CREATED);
    }
    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<SalePointDto> updateSalePoint(@RequestBody SalePointDto dto) {
        SalePointDto savedSalePoint = supplierService.updateSalePoint(dto);
        return new ResponseEntity<>(savedSalePoint, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<SalePointDto> getSalePoint(@PathVariable Long id)
    {
        SalePointDto foundSalePoint = salePointService.checkedFindById(id);
        return new ResponseEntity<>(foundSalePoint, HttpStatus.CREATED);
    }
    @GetMapping
    public List<SalePointDto> listSalePoint(@PathVariable Long supplierId) {
        return salePointService.findBySupplier(supplierId);
    }
}
