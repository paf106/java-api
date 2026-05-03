package com.solera.apitest.orders.infrastructure.persistence.adapters;

import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.domain.repositories.OrderRepository;
import com.solera.apitest.orders.infrastructure.mappers.OrderEntityMapper;
import com.solera.apitest.orders.infrastructure.persistence.entities.OrderEntity;
import com.solera.apitest.orders.infrastructure.repositories.JpaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Primary
public class PostgresOrderAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);
        OrderEntity savedOrderEntity = jpaOrderRepository.save(orderEntity);
        return orderEntityMapper.toDomain(savedOrderEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaOrderRepository.findById(id).map(orderEntityMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpaOrderRepository.findAll()
                .stream()
                .map(orderEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return jpaOrderRepository.findByUserId(userId)
                .stream()
                .map(orderEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaOrderRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaOrderRepository.existsById(id);
    }
}
