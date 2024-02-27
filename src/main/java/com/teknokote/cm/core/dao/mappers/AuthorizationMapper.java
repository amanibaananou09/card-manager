package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Authorization;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.dto.AuthorizationDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface AuthorizationMapper extends BidirectionalEntityDtoMapper<Long, Authorization, AuthorizationDto>
{
}
