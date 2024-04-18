package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.ProductDto;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SupplierService extends ActivatableEntityService<Long, SupplierDto>, BaseService<Long, SupplierDto>
{
    SupplierDto findByReference(String reference);

    SupplierDto findByReferenceAndName(String reference, String name);

    SupplierDto updateSupplier(SupplierDto supplierDto);
    SupplierDto createSupplier(SupplierDto supplierDto);
    SalePointDto createSalePoint(SalePointDto salePointDto);
    UserDto createUser(UserDto salePointDto);
    ProductDto addProduct(ProductDto productDto);

}
