package com.solera.apitest.orders.presentation.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequestDto(
        @NotEmpty(message = "El pedido debe incluir al menos un producto")
        List<@Valid CreateOrderItemRequestDto> items
) {
}
