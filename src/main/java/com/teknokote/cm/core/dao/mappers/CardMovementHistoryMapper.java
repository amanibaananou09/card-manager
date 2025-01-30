package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.CardMovementHistory;
import com.teknokote.cm.dto.CardMovementHistoryDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CardMovementHistoryMapper extends BidirectionalEntityDtoMapper<Long, CardMovementHistory, CardMovementHistoryDto> {
}
