package com.solera.apitest.orders.presentation.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long userId,
        String status,
        Double total,
        LocalDateTime createdAt,
        List<OrderItemResponseDto> items
) {
}
