package com.solera.apitest.orders.application.usecases;

import com.solera.apitest.orders.domain.exceptions.InvalidOrderException;
import com.solera.apitest.orders.domain.models.Order;
import com.solera.apitest.orders.domain.models.OrderItem;
import com.solera.apitest.orders.domain.models.OrderStatus;
import com.solera.apitest.orders.domain.repositories.OrderRepository;
import com.solera.apitest.product.domain.exceptions.ProductNotFoundException;
import com.solera.apitest.product.domain.models.Product;
import com.solera.apitest.product.domain.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void should_create_order_with_multiple_products() {
        Product laptop = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(700.0)
                .build();
        Product mouse = Product.builder()
                .id(2L)
                .name("Mouse")
                .price(25.0)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(laptop));
        when(productRepository.findById(2L)).thenReturn(Optional.of(mouse));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(10L);
            return order;
        });

        Order result = createOrderUseCase.execute(5L, List.of(
                OrderItem.builder().productId(1L).quantity(1).build(),
                OrderItem.builder().productId(2L).quantity(2).build()
        ));

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(5L, result.getUserId());
        assertEquals(OrderStatus.CREATED, result.getStatus());
        assertEquals(750.0, result.getTotal());
        assertEquals(2, result.getItems().size());
        assertEquals("Laptop", result.getItems().get(0).getProductName());
        assertEquals(700.0, result.getItems().get(0).getUnitPrice());
        assertEquals(50.0, result.getItems().get(1).getSubtotal());
    }

    @Test
    void should_throw_when_order_is_empty() {
        assertThrows(
                InvalidOrderException.class,
                () -> createOrderUseCase.execute(5L, List.of())
        );

        verify(orderRepository, never()).save(any());
    }

    @Test
    void should_throw_when_product_does_not_exist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> createOrderUseCase.execute(5L, List.of(
                        OrderItem.builder().productId(99L).quantity(1).build()
                ))
        );

        verify(orderRepository, never()).save(any());
    }
}
