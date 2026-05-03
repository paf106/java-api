package com.solera.apitest.orders.domain.repositories;

import com.solera.apitest.orders.domain.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findByUserId(Long userId);

    void deleteById(Long id);

    boolean existsById(Long id);
}
