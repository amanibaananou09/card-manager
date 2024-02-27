package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Movement;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.dto.MovementDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface MovementMapper extends BidirectionalEntityDtoMapper<Long, Movement, MovementDto>
{
}
