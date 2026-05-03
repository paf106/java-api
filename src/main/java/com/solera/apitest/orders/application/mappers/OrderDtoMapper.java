package com.solera.apitest.orders.application.mappers;

import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.domain.models.OrderItem;
import com.solera.apitest.orders.presentation.dtos.CreateOrderItemRequestDto;
import com.solera.apitest.orders.presentation.dtos.CreateOrderRequestDto;
import com.solera.apitest.orders.presentation.dtos.OrderItemResponseDto;
import com.solera.apitest.orders.presentation.dtos.OrderResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDtoMapper {

    public Order toDomain(CreateOrderRequestDto dto) {
        return Order.builder()
                .items(toDomainItems(dto.items()))
                .build();
    }

    public OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getUserId(),
                order.getStatus().name(),
                order.getTotal(),
                order.getCreatedAt(),
                toDtoItems(order.getItems())
        );
    }

    private List<OrderItem> toDomainItems(List<CreateOrderItemRequestDto> items) {
        return items.stream()
                .map(this::toDomainItem)
                .toList();
    }

    private OrderItem toDomainItem(CreateOrderItemRequestDto item) {
        return OrderItem.builder()
                .productId(item.productId())
                .quantity(item.quantity())
                .build();
    }

    private List<OrderItemResponseDto> toDtoItems(List<OrderItem> items) {
        return items.stream()
                .map(this::toDtoItem)
                .toList();
    }

    private OrderItemResponseDto toDtoItem(OrderItem item) {
        return new OrderItemResponseDto(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}
