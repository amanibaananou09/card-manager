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
        return super.beforeCreate(supplier, dto);
    }
}
