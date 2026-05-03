package com.solera.apitest.orders.presentation.dtos;

public record OrderItemResponseDto(
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        Double unitPrice,
        Double subtotal
) {
}
