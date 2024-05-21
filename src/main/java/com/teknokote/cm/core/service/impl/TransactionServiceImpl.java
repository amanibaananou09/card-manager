package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.service.ProductService;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.core.service.TransactionService;
import com.teknokote.cm.dto.ProductDto;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class TransactionServiceImpl extends GenericCheckedService<Long, TransactionDto> implements TransactionService
{
   @Autowired
   private ESSValidator<TransactionDto> validator;
   @Autowired
   private TransactionDao dao;
   @Autowired
   private SupplierService supplierService;
   @Autowired
   private ProductService productService;

   @Override
   public TransactionDto createTransaction(TransactionDto dto) {
      SupplierDto supplierDto= supplierService.findByReference(dto.getReference());
      if (supplierDto!=null){
         ProductDto productDto = productService.findBySupplierAndName(dto.getProductName(),supplierDto.getId());
         if (productDto!=null){
            dto.setProductId(productDto.getId());
         }
      }
      return create(dto);
   }
}
