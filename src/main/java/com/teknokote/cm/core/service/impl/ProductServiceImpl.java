package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.ProductDao;
import com.teknokote.cm.core.service.ProductService;
import com.teknokote.cm.dto.ProductDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ProductServiceImpl extends GenericCheckedService<Long, ProductDto> implements ProductService
{
   @Autowired
   private ESSValidator<ProductDto> validator;
   @Autowired
   private ProductDao dao;
   @Override
   public ProductDto findProductWithName(String name,Long supplierId) {
      return getDao().findProductWithName(name,supplierId);
   }
}
