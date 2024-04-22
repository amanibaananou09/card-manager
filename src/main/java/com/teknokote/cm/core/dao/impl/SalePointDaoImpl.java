package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.SalePointDao;
import com.teknokote.cm.core.dao.mappers.SalePointMapper;
import com.teknokote.cm.core.model.SalePoint;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.repository.SalePointRepository;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.cm.dto.UserDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Getter
@Setter
public class SalePointDaoImpl extends JpaGenericDao<Long,SalePointDto, SalePoint> implements SalePointDao
{
    @Autowired
    private SalePointMapper mapper;
    @Autowired
    private SalePointRepository repository;
    @Override
    protected SalePoint beforeCreate(SalePoint salePoint, SalePointDto dto) {
        return super.beforeCreate(salePoint,dto);
    }

    @Override
    public List<SalePointDto> findBySupplier(Long supplierId) {
        return getRepository().findAllBySupplierId(supplierId).stream().map(getMapper()::toDto).collect(Collectors.toList());
    }
}
