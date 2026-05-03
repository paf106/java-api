package com.solera.apitest.orders.presentation.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemRequestDto(
        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El ID de producto debe ser positivo")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que 0")
        Integer quantity
) {
}
