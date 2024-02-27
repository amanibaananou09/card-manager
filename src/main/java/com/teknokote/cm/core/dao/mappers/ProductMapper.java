package com.teknokote.cm.core.dao.mappers;

import com.teknokote.core.config.MapperConfiguration;
import com.teknokote.core.mappers.BidirectionalEntityDtoMapper;
import com.teknokote.cm.core.model.Product;
import com.teknokote.cm.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface ProductMapper extends BidirectionalEntityDtoMapper<Long, Product, ProductDto>
{
}
