package com.teknokote.cm.core.dao.mappers;

import com.teknokote.cm.core.model.Customer;
import com.teknokote.cm.dto.CustomerDto;
import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface CustomerMapper extends BidirectionalEntityDtoMapper<Long, Customer, CustomerDto> {
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Override
    CustomerDto toDto(Customer source);
}
