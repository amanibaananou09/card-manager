package com.teknokote.cm.core.service;

import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.core.service.BaseService;

import java.util.List;

public interface SalePointService extends BaseService<Long, SalePointDto>
{
    List<SalePointDto> findBySupplier(Long supplierId);
}
