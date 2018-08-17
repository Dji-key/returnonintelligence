package ru.returnonintelligence.catalog.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.returnonintelligence.catalog.entity.Product;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProductSpecs {
    public static Specification<Product> partOfName(String name) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("name"), "%"+name+"%");
            }
        };
    }
}
