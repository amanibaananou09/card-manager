package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.ProductService;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.dto.ProductDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.PRODUCT_ROOT)
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierService supplierService;


    @PostMapping(EndPoints.ADD)
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto dto) {
        ProductDto savedProduct = supplierService.addProduct(dto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto dto) {
        ProductDto savedProduct = productService.update(dto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping(EndPoints.INFO)
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto foundProduct = productService.checkedFindById(id);
        return new ResponseEntity<>(foundProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProductDto> listProduct() {
        return productService.findAll();
    }

    @GetMapping(EndPoints.LIST_PRODUCT_BY_SUPPLIER)
    public List<ProductDto> listProductBySupplier(@PathVariable Long supplierId) {
        return productService.findBySupplier(supplierId);
    }
}
