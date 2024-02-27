package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Account;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.dto.AccountDto;
import com.teknokote.core.config.MapperConfiguration;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface AccountMapper extends BidirectionalEntityDtoMapper<Long, Account, AccountDto>
{
}
