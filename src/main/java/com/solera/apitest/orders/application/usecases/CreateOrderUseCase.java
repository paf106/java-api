package com.solera.apitest.orders.application.usecases;

import com.solera.apitest.orders.domain.exceptions.InvalidOrderException;
import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.domain.models.OrderItem;
import com.solera.apitest.orders.domain.models.OrderStatus;
import com.solera.apitest.orders.domain.repositories.OrderRepository;
import com.solera.apitest.product.domain.exceptions.ProductNotFoundException;
import com.solera.apitest.product.domain.models.Product;
import com.solera.apitest.product.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order execute(Long userId, List<OrderItem> requestedItems) {
        if (userId == null) {
            throw new InvalidOrderException("No se puede crear un pedido sin usuario autenticado");
        }

        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new InvalidOrderException("El pedido debe incluir al menos un producto");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (OrderItem requestedItem : requestedItems) {
            validateItem(requestedItem);

            Product product = productRepository.findById(requestedItem.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(requestedItem.getProductId()));
            int quantity = requestedItem.getQuantity();
            double subtotal = product.getPrice() * quantity;

            orderItems.add(OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .subtotal(subtotal)
                    .build());
            total += subtotal;
        }

        Order order = Order.builder()
                .userId(userId)
                .items(orderItems)
                .total(total)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }

    private void validateItem(OrderItem item) {
        if (item == null || item.getProductId() == null) {
            throw new InvalidOrderException("Cada línea debe indicar un producto");
        }

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new InvalidOrderException("La cantidad debe ser mayor que 0");
        }
    }
}
