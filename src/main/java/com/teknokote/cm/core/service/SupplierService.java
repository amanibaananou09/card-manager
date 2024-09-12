package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.ProductDto;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.service.ActivatableEntityService;
import com.teknokote.core.service.BaseService;

public interface SupplierService extends ActivatableEntityService<Long, SupplierDto>, BaseService<Long, SupplierDto>
{
    SupplierDto findByReference(String reference);

    SupplierDto findByReferenceAndIdentifier(String reference, String name);

    SupplierDto updateSupplier(SupplierDto supplierDto);
    SupplierDto createSupplier(SupplierDto supplierDto);
    SalePointDto createSalePoint(Long supplierId,SalePointDto salePointDto);
    SalePointDto updateSalePoint(Long supplierId,SalePointDto salePointDto);
    UserDto createUser(Long supplierId,UserDto salePointDto);
    UserDto updateUser(Long supplierId,UserDto salePointDto);
    ProductDto addProduct(ProductDto productDto);

}
