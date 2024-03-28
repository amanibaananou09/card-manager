package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.CardGroup;
import com.teknokote.cm.dto.CardGroupDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CardGroupMapper extends BidirectionalEntityDtoMapper<Long, CardGroup, CardGroupDto>
{
}
