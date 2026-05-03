package com.solera.apitest.orders.infrastructure.repositories;


import com.solera.apitest.orders.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Override
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<OrderEntity> findById(@Param("id") Long id);

    @Override
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items")
    List<OrderEntity> findAll();

    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<OrderEntity> findByUserId(@Param("userId") Long userId);
}
