package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.ProductDto;
import com.teknokote.core.service.BaseService;

import java.util.List;

public interface ProductService extends BaseService<Long, ProductDto> {
    List<ProductDto> findBySupplier(Long supplierId);

    ProductDto findBySupplierAndName(String productName,Long SupplierId);
}
