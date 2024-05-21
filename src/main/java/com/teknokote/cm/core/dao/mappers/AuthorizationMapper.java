package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Authorization;
import com.teknokote.cm.dto.AuthorizationDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface AuthorizationMapper extends BidirectionalEntityDtoMapper<Long, Authorization, AuthorizationDto>
{
    @Override
    @Mapping(source = "card.type",target = "cardType")
    AuthorizationDto toDto(Authorization source);
}
