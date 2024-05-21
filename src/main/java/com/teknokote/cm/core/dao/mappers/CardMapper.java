package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Card;
import com.teknokote.cm.dto.CardDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface CardMapper extends BidirectionalEntityDtoMapper<Long, Card, CardDto> {
    @Mapping(source = "cardGroup.name", target = "cardGroupName")
    @Override
    CardDto toDto(Card source);
}
