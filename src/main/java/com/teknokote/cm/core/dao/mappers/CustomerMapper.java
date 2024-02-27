package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Customer;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CustomerMapper extends BidirectionalEntityDtoMapper<Long, Customer, CustomerDto>
{
}
