package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.UserHistory;
import com.teknokote.cm.dto.UserHistoryDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserHistoryMapper extends BidirectionalEntityDtoMapper<Long, UserHistory, UserHistoryDto>
{
}
