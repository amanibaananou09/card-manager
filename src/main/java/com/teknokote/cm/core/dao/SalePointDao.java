package com.teknokote.cm.core.dao;

import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.core.dao.BasicDao;

import java.util.List;


public interface SalePointDao extends BasicDao<Long, SalePointDto>
{
    List<SalePointDto> findBySupplier(Long supplierId);
}

