package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.SupplierDao;
import com.teknokote.cm.core.dao.mappers.SupplierMapper;
import com.teknokote.cm.core.dao.mappers.UserMapper;
import com.teknokote.cm.core.model.Country;
import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.SupplierRepository;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.dao.JpaActivatableGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;

@Repository
@Getter
@Setter
public class SupplierDaoImpl extends JpaActivatableGenericDao<Long,User ,SupplierDto, Supplier> implements SupplierDao
{
    @Autowired
    private SupplierMapper mapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SupplierRepository repository;
    @Override
    protected Supplier beforeCreate(Supplier supplier, SupplierDto dto) {
        supplier.setActif(true);
        supplier.setDateStatusChange(LocalDateTime.now());
        Supplier savedSupplier = super.beforeCreate(supplier, dto);
        if (Objects.nonNull(dto.getSalePoints())) {
            savedSupplier.getSalePoints().forEach(salePoint -> {
                salePoint.setSupplier(savedSupplier);
                SalePointDto salePointDto = dto.getSalePoints().stream()
                        .filter(spDto -> spDto.getIdentifier().equals(salePoint.getIdentifier()))
                        .findFirst()
                        .orElse(null);
                if (salePointDto != null && salePointDto.getCountryId() != null) {
                    salePoint.setCountry(getEntityManager().getReference(Country.class,salePointDto.getCountryId()));
                }
            });
        }
        if (Objects.nonNull(dto.getUsers())) {
            savedSupplier.getUsers().forEach(user -> user.setDateStatusChange(LocalDateTime.now()));
        }

        return savedSupplier;
    }
    @Override
    protected Supplier beforeUpdate(Supplier supplier, SupplierDto dto) {
        supplier.setDateStatusChange(LocalDateTime.now());
        Supplier savedSupplier = super.beforeUpdate(supplier, dto);
        if (Objects.nonNull(dto.getSalePoints())) {
            savedSupplier.getSalePoints().forEach(salePoint -> salePoint.setSupplier(savedSupplier));
        }
        if (Objects.nonNull(dto.getUsers())) {
            savedSupplier.getUsers().forEach(user -> user.setDateStatusChange(LocalDateTime.now()));
        }
        return savedSupplier;
    }

    @Override
    public SupplierDto findAllByReference(String reference) {
        return getMapper().toDto(getRepository().findByReference(reference));
    }

    @Override
    public SupplierDto findAllByReferenceAndIdentifier(String reference, String identifier) {
        Supplier supplier=getRepository().findByReferenceAndIdentifier(reference,identifier);
        return getMapper().toDto(supplier);
    }
}
