package com.teknokote.cm.core.dao.mappers;

import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.dto.TransactionDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface TransactionMapper extends BidirectionalEntityDtoMapper<Long, Transaction, TransactionDto>
{
}
