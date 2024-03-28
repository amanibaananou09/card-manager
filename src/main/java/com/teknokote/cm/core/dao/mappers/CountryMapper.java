package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Country;
import com.teknokote.cm.dto.CountryDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CountryMapper extends BidirectionalEntityDtoMapper<Long, Country, CountryDto>
{
}
