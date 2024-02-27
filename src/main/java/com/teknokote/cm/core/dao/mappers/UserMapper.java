package com.teknokote.cm.core.dao.mappers;

import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper extends BidirectionalEntityDtoMapper<Long, User, UserDto>
{
}
