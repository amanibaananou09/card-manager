package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.ProductDto;
import com.teknokote.core.dao.BasicDao;

import java.util.List;


public interface ProductDao extends BasicDao<Long, ProductDto> {
    ProductDto findProductWithName(String name, Long supplierId);

    List<ProductDto> findBySupplier(Long supplierId);

}

