package com.solera.apitest.orders.presentation;

import com.solera.apitest.orders.application.mappers.OrderDtoMapper;
import com.solera.apitest.orders.application.usecases.CreateOrderUseCase;
import com.solera.apitest.orders.application.usecases.GetAllOrdersUseCase;
import com.solera.apitest.orders.application.usecases.GetOrderByIdUseCase;
import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.presentation.dtos.CreateOrderRequestDto;
import com.solera.apitest.orders.presentation.dtos.OrderResponseDto;
import com.solera.apitest.users.domain.models.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order", description = "Order API operations")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final OrderDtoMapper orderDtoMapper;

    @PostMapping("")
    ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrderRequestDto request
    ) {
        Order orderToCreate = orderDtoMapper.toDomain(request);
        Order order = createOrderUseCase.execute(user.getId(), orderToCreate.getItems());
        OrderResponseDto response = orderDtoMapper.toDto(order);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("")
    ResponseEntity<List<OrderResponseDto>> getMyOrders(@AuthenticationPrincipal User user) {
        List<OrderResponseDto> response = getAllOrdersUseCase.execute(user.getId())
                .stream()
                .map(orderDtoMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    ResponseEntity<OrderResponseDto> getOrderById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        Order order = getOrderByIdUseCase.execute(user.getId(), id);

        return ResponseEntity.ok(orderDtoMapper.toDto(order));
    }

}
