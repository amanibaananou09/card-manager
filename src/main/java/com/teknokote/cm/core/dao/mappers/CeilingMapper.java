package com.teknokote.cm.core.dao.mappers;

import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.core.model.Ceiling;
import com.teknokote.cm.dto.CeilingDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CeilingMapper extends BidirectionalEntityDtoMapper<Long, Ceiling, CeilingDto>
{
}
