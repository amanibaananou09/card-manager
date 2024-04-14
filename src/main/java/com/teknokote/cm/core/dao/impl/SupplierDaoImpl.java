package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.mappers.SupplierMapper;
import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.SupplierRepository;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@Getter
@Setter
public class SupplierDaoImpl extends JpaActivatableGenericDao<Long,User ,SupplierDto, Supplier> implements SupplierDao
{
    @Autowired
    private SupplierMapper mapper;
    @Autowired
    private SupplierRepository repository;
    @Override
    protected Supplier beforeCreate(Supplier supplier, SupplierDto dto) {
        supplier.setActif(true);
        supplier.setDateStatusChange(LocalDateTime.now());
        Supplier savedSupplier = super.beforeCreate(supplier, dto);
        if (!dto.getSalePoints().isEmpty()) {
            savedSupplier.getSalePoints().forEach(salePoint -> salePoint.setSupplier(savedSupplier));
        }
        if (!dto.getUsers().isEmpty()) {
            savedSupplier.getUsers().forEach(user -> user.setDateStatusChange(LocalDateTime.now()));
        }

        return savedSupplier;
    }
    @Override
    protected Supplier beforeUpdate(Supplier supplier, SupplierDto dto) {
        supplier.setDateStatusChange(LocalDateTime.now());
        Supplier savedSupplier = super.beforeCreate(supplier, dto);
        if (!dto.getSalePoints().isEmpty()) {
            savedSupplier.getSalePoints().forEach(salePoint -> salePoint.setSupplier(savedSupplier));
        }
        if (!dto.getUsers().isEmpty()) {
            savedSupplier.getUsers().forEach(user -> user.setDateStatusChange(LocalDateTime.now()));
        }
        return savedSupplier;
    }

    @Override
    public SupplierDto findAllByReference(String reference) {
        return getMapper().toDto(getRepository().findAllByReference(reference));
    }
}
