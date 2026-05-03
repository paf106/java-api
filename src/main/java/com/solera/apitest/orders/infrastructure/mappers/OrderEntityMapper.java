package com.solera.apitest.orders.infrastructure.mappers;

import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.domain.models.OrderItem;
import com.solera.apitest.orders.infrastructure.persistence.entities.OrderEntity;
import com.solera.apitest.orders.infrastructure.persistence.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderEntityMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUserId(order.getUserId());
        entity.setStatus(order.getStatus());
        entity.setTotal(order.getTotal());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setItems(toEntityItems(order.getItems(), entity));
        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .status(entity.getStatus())
                .total(entity.getTotal())
                .createdAt(entity.getCreatedAt())
                .items(toDomainItems(entity.getItems()))
                .build();
    }

    private List<OrderItemEntity> toEntityItems(List<OrderItem> items, OrderEntity orderEntity) {
        if (items == null) {
            return new ArrayList<>();
        }

        List<OrderItemEntity> itemEntities = new ArrayList<>();
        for (OrderItem item : items) {
            itemEntities.add(toEntityItem(item, orderEntity));
        }
        return itemEntities;
    }

    private OrderItemEntity toEntityItem(OrderItem item, OrderEntity orderEntity) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(item.getId());
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setSubtotal(item.getSubtotal());
        entity.setOrder(orderEntity);
        return entity;
    }

    private List<OrderItem> toDomainItems(List<OrderItemEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDomainItem)
                .toList();
    }

    private OrderItem toDomainItem(OrderItemEntity entity) {
        return OrderItem.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .subtotal(entity.getSubtotal())
                .build();
    }
}
