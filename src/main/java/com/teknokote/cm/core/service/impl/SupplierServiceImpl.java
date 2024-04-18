package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.ProductDao;
import com.teknokote.cm.core.dao.SalePointDao;
import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.dto.ProductDto;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ActivatableGenericCheckedService;
import com.teknokote.core.service.ESSValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
public class SupplierServiceImpl extends ActivatableGenericCheckedService<Long, SupplierDto> implements SupplierService
{
    @Autowired
    private ESSValidator<SupplierDto> validator;
    @Autowired
    private SupplierDao dao;
    @Autowired
    private SalePointDao salePointDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public SupplierDto findByReference(String reference) {
        return getDao().findAllByReference(reference);
    }
    @Override
    public SupplierDto findByReferenceAndName(String reference, String name) {
        return getDao().findAllByReferenceAndName(reference,name);
    }
    @Transactional
    @Override
    public SupplierDto updateSupplier(SupplierDto supplierDto) {
        SupplierDto supplier = findByReferenceAndName(supplierDto.getReference(),supplierDto.getName());
        if (supplier!=null){
            supplierDto.setId(supplier.getId());
            return update(supplierDto);
        }
        return null;
    }

    @Override
    public SupplierDto createSupplier(SupplierDto supplierDto) {
        SupplierDto supplier = findByReferenceAndName(supplierDto.getReference(),supplierDto.getName());
        if (supplier!=null){
           throw new ServiceValidationException("supplier already exported") ;
        }
        return create(supplierDto);
    }


    @Override
    @Transactional
    public SalePointDto createSalePoint(SalePointDto salePointDto) {
        SupplierDto supplier = findByReference(salePointDto.getReference());
        if (supplier!=null){
            salePointDto.setSupplierId(supplier.getId());
            return salePointDao.create(salePointDto);
        }
        return null;
    }
    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        SupplierDto supplier = findByReference(userDto.getReference());
        if (supplier!=null){
            return userDao.create(userDto);
        }
        return null;
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        SupplierDto supplier = findByReference(productDto.getReference());
        if (supplier!=null){
            return productDao.create(productDto);
        }
        return null;
    }
}
