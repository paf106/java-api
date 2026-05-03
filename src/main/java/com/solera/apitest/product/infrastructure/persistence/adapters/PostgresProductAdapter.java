package com.solera.apitest.product.infrastructure.persistence.adapters;

import com.solera.apitest.categories.infrastructure.persistence.entities.CategoryEntity;
import com.solera.apitest.product.application.mappers.ProductDtoMapper;
import com.solera.apitest.product.domain.models.Product;
import com.solera.apitest.product.domain.repositories.ProductRepository;
import com.solera.apitest.product.infrastructure.mappers.ProductEntityMapper;
import com.solera.apitest.product.infrastructure.persistence.entities.ProductEntity;
import com.solera.apitest.product.infrastructure.repositories.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class PostgresProductAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = productEntityMapper.toEntity(product);
        ProductEntity savedProductEntity = jpaProductRepository.save(productEntity);
        return productEntityMapper.toDomain(savedProductEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaProductRepository.findById(id).map(productEntityMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        List<ProductEntity> productEntities = jpaProductRepository.findAll();
        return productEntities.stream()
                .map(productEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        // TODO check if category exists
        List<ProductEntity> productEntities = jpaProductRepository.findByCategoryId(categoryId);

        return productEntities.stream()
                .map(productEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaProductRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaProductRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaProductRepository.existsByName(name);
    }
}
