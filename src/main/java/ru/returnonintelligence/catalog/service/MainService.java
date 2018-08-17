package ru.returnonintelligence.catalog.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.returnonintelligence.catalog.entity.Category;
import ru.returnonintelligence.catalog.entity.Product;
import ru.returnonintelligence.catalog.repository.CategoryRepository;
import ru.returnonintelligence.catalog.repository.ProductRepository;
import ru.returnonintelligence.catalog.repository.ProductSpecs;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class MainService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private SessionFactory sessionFactory;

    public void save(Product product, Long[] categoriesId) {
        Set<Category> categories = new HashSet<>();
        if (categoriesId != null) {
            for (Long id : categoriesId) {
                categories.add(categoryRepository.getById(id));
            }
            product.setCategories(categories);
        }
        productRepository.save(product);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public Iterable<Product> findProductsLikeName(String name) {
        return productRepository.findAll(ProductSpecs.partOfName(name));
    }

    public Iterable<Product> fingProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            for (Category existCategory : product.getCategories()) {
                if (existCategory.getName().equals(category)) {
                    result.add(product);
                }
            }
        }
        return result;
    }

    public Iterable<Product> merge(Iterable<Product> A, Iterable<Product> B) {
        ArrayList<Product> result = new ArrayList<>();
        for (Product first : A) {
            for (Product second : B) {
                if (first.equals(second)) {
                    result.add(first);
                }
            }
        }
        return result;
    }

    public Iterable<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Iterable<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long productID) {
        productRepository.deleteById(productID);
    }

    public Product getProductById(Long productID) {
        return productRepository.getById(productID);
    }

    public Long[] concat(Long[] A, Long[] B) {
        if (A == null) {
            return B;
        } else if (B == null) {
            return A;
        }
        int aLen = A.length;
        int bLen = B.length;
        Long[] C = new Long[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
}
