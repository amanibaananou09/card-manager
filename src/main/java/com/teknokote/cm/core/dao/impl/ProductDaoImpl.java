package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.ProductDao;
import com.teknokote.cm.core.dao.mappers.ProductMapper;
import com.teknokote.cm.core.model.Product;
import com.teknokote.cm.core.repository.ProductRepository;
import com.teknokote.cm.dto.ProductDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class ProductDaoImpl extends JpaGenericDao<Long, ProductDto, Product> implements ProductDao
{
   @Autowired
   private ProductMapper mapper;
   @Autowired
   private ProductRepository repository;

   @Override
   public ProductDto findProductWithName(String name,Long supplierId) {
      return getMapper().toDto(getRepository().findAllByNameAndSupplier(name,supplierId));
   }
}
