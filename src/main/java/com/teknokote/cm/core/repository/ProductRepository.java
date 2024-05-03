package com.teknokote.cm.core.repository;

import com.teknokote.cm.core.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p from Product p where p.name = :name and p.supplierId = :supplierId ")
    Product findAllByNameAndSupplier(String name, Long supplierId);

    List<Product> findAllBySupplierId(Long supplierId);
}
