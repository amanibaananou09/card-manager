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
import java.util.Set;

@Service
@Getter
public class SupplierServiceImpl extends ActivatableGenericCheckedService<Long, SupplierDto> implements SupplierService {
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
        return getDao().findAllByReferenceAndName(reference, name);
    }

    @Transactional
    @Override
    public SupplierDto updateSupplier(SupplierDto supplierDto) {
        SupplierDto supplier = findByReferenceAndName(supplierDto.getReference(), supplierDto.getName());
        if (supplier != null) {
            supplierDto.setId(supplier.getId());
            return update(supplierDto);
        } else {
            throw new ServiceValidationException("supplier not existing on card manager !!!");
        }
    }

    @Override
    public SupplierDto createSupplier(SupplierDto supplierDto) {
        SupplierDto supplier = findByReferenceAndName(supplierDto.getReference(), supplierDto.getName());
        if (supplier != null) {
            throw new ServiceValidationException("supplier already exported");
        }
        return create(supplierDto);
    }


    @Override
    @Transactional
    public SalePointDto createSalePoint(Long supplierId,SalePointDto salePointDto) {
        SupplierDto supplier = checkedFindById(supplierId);
        if (supplier != null) {
            salePointDto.setSupplierId(supplier.getId());
            return salePointDao.create(salePointDto);
        }else {
            throw new ServiceValidationException("supplier not existing on card manager !!!");
        }
    }

    @Override
    @Transactional
    public SalePointDto updateSalePoint(Long supplierId,SalePointDto salePointDto) {
        SupplierDto supplier = checkedFindById(supplierId);
        if (supplier != null) {
            List<SalePointDto> salePoints = supplier.getSalePoints().stream().filter(salePointDto1 -> salePointDto1.getName().equals(salePointDto.getName())).toList();
            if (!salePoints.isEmpty()){
                SalePointDto existingSalePoint = salePoints.get(0);
                salePointDto.setId(existingSalePoint.getId());
                return salePointDao.update(salePointDto);
            }
        }
        return null;
    }

    @Override
    public UserDto createUser(Long supplierId,UserDto userDto) {
        SupplierDto supplier = checkedFindById(supplierId);
        Set<UserDto> existingUsers = supplier.getUsers();
        if (supplier != null) {
            supplier.getUsers().add(userDto);
            SupplierDto supplierDto=dao.update(supplier);
            for (UserDto existingUser : existingUsers.stream().filter(userDto1 -> userDto1.getId()!=null).toList()) {
                if (!supplierDto.getUsers().contains(existingUser)) {
                    supplier.getUsers().remove(existingUser);
                    userDao.deleteById(existingUser.getId());
                }
            }
            }else{
                throw new ServiceValidationException("supplier not existing on card manager !!!");
            }
        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(Long supplierId,UserDto userDto) {
        SupplierDto supplier = checkedFindById(supplierId);
        if (supplier != null) {
            UserDto existingUser = supplier.getUsers().stream().filter(userDto1 -> userDto1.getUsername().equals(userDto.getUsername())).toList().get(0);
            if (existingUser != null) {
                userDto.setId(existingUser.getId());
                return userDao.update(userDto);
            }
        }
        return userDto;
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        SupplierDto supplier = findByReference(productDto.getReference());
        if (supplier != null) {
            productDto.setSupplierId(supplier.getId());
            ProductDto product = productDao.findProductWithName(productDto.getName(), supplier.getId());
            if (product != null) {
                productDto.setId(product.getId());
                return productDao.update(productDto);
            }
            return productDao.create(productDto);
        }else {
            throw new ServiceValidationException("supplier not existing on card manager !!!");
        }
    }
}
