package com.solera.apitest.orders.application.usecases;

import com.solera.apitest.orders.domain.exceptions.OrderNotFoundException;
import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.domain.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOrderByIdUseCase {

    private final OrderRepository orderRepository;

    public Order execute(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException(orderId);
        }

        return order;
    }
}
