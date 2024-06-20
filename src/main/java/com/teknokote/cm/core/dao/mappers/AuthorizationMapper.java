package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Authorization;
import com.teknokote.cm.core.model.Card;
import com.teknokote.cm.core.model.EnumCardType;
import com.teknokote.cm.core.model.EnumCeilingType;
import com.teknokote.cm.dto.AuthorizationDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface AuthorizationMapper extends BidirectionalEntityDtoMapper<Long, Authorization, AuthorizationDto>
{
    @Mapping(source = "card", target = "cardType", qualifiedByName = "mapCardToCardType")
    AuthorizationDto toDto(Authorization source);

    @Named("mapCardToCardType")
    default EnumCardType mapCardToCardType(Card card) {
        EnumCeilingType ceilingType = card.getFirstCeilingType();
        return convertCeilingTypeToCardType(ceilingType);
    }
    default EnumCardType convertCeilingTypeToCardType(EnumCeilingType ceilingType) {
        if (ceilingType != null) {
            switch (ceilingType) {
                case AMOUNT:
                    return EnumCardType.AMOUNT;
                case VOLUME:
                    return EnumCardType.QUANTITY;
                default:
                    return null;
            }
        }
        return null; // or a default value
    }
}
