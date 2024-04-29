package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.SalePoint;
import com.teknokote.cm.dto.SalePointDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface SalePointMapper extends BidirectionalEntityDtoMapper<Long, SalePoint, SalePointDto>
{
    @Mapping(source = "country.name", target = "countryName")
    SalePointDto toDto(SalePoint source);
}
