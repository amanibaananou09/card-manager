package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.dto.SupplierDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface SupplierMapper extends BidirectionalEntityDtoMapper<Long, Supplier, SupplierDto>
{
}
