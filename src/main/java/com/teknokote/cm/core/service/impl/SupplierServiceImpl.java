package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.ProductDao;
import com.teknokote.cm.core.dao.SalePointDao;
import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.UserDao;
import com.teknokote.cm.core.service.interfaces.SupplierService;
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
    public SupplierDto findByReferenceAndIdentifier(String reference, String identifier) {
        return getDao().findAllByReferenceAndIdentifier(reference, identifier);
    }

    @Transactional
    @Override
    public SupplierDto updateSupplier(SupplierDto supplierDto) {
        SupplierDto supplier = findByReferenceAndIdentifier(supplierDto.getReference(), supplierDto.getIdentifier());
        if (supplier != null) {
            supplierDto.setId(supplier.getId());
            return update(supplierDto);
        } else {
            throw new ServiceValidationException("supplier not existing on card manager !!!");
        }
    }

    @Override
    public SupplierDto createSupplier(SupplierDto supplierDto) {
        SupplierDto supplier = findByReferenceAndIdentifier(supplierDto.getReference(), supplierDto.getIdentifier());
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
            List<SalePointDto> salePoints = supplier.getSalePoints().stream().filter(salePointDto1 -> salePointDto1.getIdentifier().equals(salePointDto.getIdentifier())).toList();
            if (!salePoints.isEmpty()){
                SalePointDto existingSalePoint = salePoints.get(0);
                salePointDto.setId(existingSalePoint.getId());
                return salePointDao.update(salePointDto);
            }
        }
        return null;
    }

    @Override
    public UserDto createUser(Long supplierId, UserDto userDto) {
        SupplierDto supplier = checkedFindById(supplierId);
        if (supplier == null) {
            throw new ServiceValidationException("supplier not existing on card manager !!!");
        }

        Set<UserDto> existingUsers = supplier.getUsers();
        supplier.getUsers().add(userDto);
        SupplierDto supplierDto = dao.update(supplier);

        // Cleanup des users deleted
        existingUsers.stream()
                .filter(user -> user.getId() != null && !supplierDto.getUsers().contains(user))
                .forEach(user -> {
                    supplier.getUsers().remove(user);
                    userDao.deleteById(user.getId());
                });

        return userDto;
    }


    @Override
    @Transactional
    public UserDto updateUser(Long supplierId, UserDto userDto) {
        SupplierDto supplier = checkedFindById(supplierId);
        if (supplier != null) {
            List<UserDto> matchingUsers = supplier.getUsers().stream()
                    .filter(userDto1 -> userDto1.getUserIdentifier() != null && userDto1.getUserIdentifier().equals(userDto.getUserIdentifier()))
                    .toList();

            if (!matchingUsers.isEmpty()) {
                UserDto existingUser = matchingUsers.get(0);
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
