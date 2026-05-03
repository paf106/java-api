package com.solera.apitest.orders.application.usecases;

import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.domain.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllOrdersUseCase {

    private final OrderRepository orderRepository;

    public List<Order> execute(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
