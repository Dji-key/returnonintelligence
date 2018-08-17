package ru.returnonintelligence.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.returnonintelligence.catalog.entity.Category;
import ru.returnonintelligence.catalog.entity.Product;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product getById(Long id);
}
