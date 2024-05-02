package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.ProductDao;
import com.teknokote.cm.core.dao.mappers.ProductMapper;
import com.teknokote.cm.core.model.Product;
import com.teknokote.cm.core.model.Supplier;
import com.teknokote.cm.core.repository.ProductRepository;
import com.teknokote.cm.dto.ProductDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Getter
@Setter
public class ProductDaoImpl extends JpaGenericDao<Long, ProductDto, Product> implements ProductDao {
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private ProductRepository repository;

    @Override
    public ProductDto findProductWithName(String name, Long supplierId) {
        return getMapper().toDto(getRepository().findAllByNameAndSupplier(name, supplierId));
    }

    @Override
    public List<ProductDto> findBySupplier(Long supplierId) {
        return getRepository().findAllBySupplierId(supplierId).stream()
                .map(product -> {
                    ProductDto productDto = getMapper().toDto(product);
                    productDto.setLastModifiedDate(product.getAudit().getLastModifiedDate());
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    protected Product beforeCreate(Product product, ProductDto dto) {
        if (Objects.nonNull(dto.getSupplierId())) {
            product.setSupplier(getEntityManager().getReference(Supplier.class, dto.getSupplierId()));
        }
        return super.beforeCreate(product, dto);
    }

    @Override
    protected Product beforeUpdate(Product product, ProductDto dto) {
        if (Objects.nonNull(dto.getSupplierId())) {
            product.setSupplier(getEntityManager().getReference(Supplier.class, dto.getSupplierId()));
        }
        return super.beforeUpdate(product, dto);
    }

}
