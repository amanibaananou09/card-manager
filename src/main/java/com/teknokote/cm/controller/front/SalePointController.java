package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.SalePointService;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.dto.SalePointDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<SalePointDto> addSalePoint(@PathVariable Long supplierId, @RequestBody SalePointDto dto) {
        SalePointDto savedSalePoint = supplierService.createSalePoint(supplierId, dto);
        return new ResponseEntity<>(savedSalePoint, HttpStatus.CREATED);
    }

    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<SalePointDto> updateSalePoint(@PathVariable Long supplierId, @RequestBody SalePointDto dto) {
        SalePointDto savedSalePoint = supplierService.updateSalePoint(supplierId, dto);
        return new ResponseEntity<>(savedSalePoint, HttpStatus.CREATED);
    }

    @GetMapping(EndPoints.INFO)
    public ResponseEntity<SalePointDto> getSalePoint(@PathVariable Long id) {
        SalePointDto foundSalePoint = salePointService.checkedFindById(id);
        return new ResponseEntity<>(foundSalePoint, HttpStatus.CREATED);
    }

    @GetMapping
    public List<SalePointDto> listSalePoint(@PathVariable Long supplierId) {
        return salePointService.findBySupplier(supplierId);
    }

    @GetMapping(EndPoints.PAGE_SALE_POINT_BY_SUPPLIER)
    public Page<SalePointDto> listSalePoint(@PathVariable Long supplierId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "25") int size) {

        List<SalePointDto> salePointList = salePointService.findBySupplier(supplierId);
        int start = page * size;
        int end = Math.min((start + size), salePointList.size());
        return new PageImpl<>(salePointList.subList(start, end), PageRequest.of(page, size), salePointList.size());
    }
}
