package com.umi.tradestar.service;

import com.umi.tradestar.dto.OrderRequest;
import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderStatus;
import com.umi.tradestar.model.enums.OrderType;
import com.umi.tradestar.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ValidRequest_ReturnsOrder() {
        // Given
        OrderRequest request = OrderRequest.builder()
                .symbol("AAPL")
                .side(OrderSide.BUY)
                .quantity(100L)
                .price(BigDecimal.valueOf(150.0))
                .type(OrderType.LIMIT)
                .clientOrderId("CLIENT123")
                .build();

        Order savedOrder = Order.builder()
                .orderId("ORDER123")
                .symbol("AAPL")
                .side(OrderSide.BUY)
                .quantity(100L)
                .price(BigDecimal.valueOf(150.0))
                .type(OrderType.LIMIT)
                .status(OrderStatus.NEW)
                .clientOrderId("CLIENT123")
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        Order result = orderService.createOrder(request);

        // Then
        assertNotNull(result);
        assertEquals("ORDER123", result.getOrderId());
        assertEquals("AAPL", result.getSymbol());
        assertEquals(OrderSide.BUY, result.getSide());
        assertEquals(100L, result.getQuantity());
        assertEquals(BigDecimal.valueOf(150.0), result.getPrice());
        assertEquals(OrderType.LIMIT, result.getType());
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertEquals("CLIENT123", result.getClientOrderId());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void modifyOrder_ValidRequest_ReturnsModifiedOrder() {
        // Given
        String orderId = "ORDER123";
        OrderRequest request = OrderRequest.builder()
                .orderId(orderId)
                .quantity(200L)
                .price(BigDecimal.valueOf(160.0))
                .build();

        Order existingOrder = Order.builder()
                .orderId(orderId)
                .symbol("AAPL")
                .side(OrderSide.BUY)
                .quantity(100L)
                .price(BigDecimal.valueOf(150.0))
                .type(OrderType.LIMIT)
                .status(OrderStatus.NEW)
                .clientOrderId("CLIENT123")
                .build();

        Order modifiedOrder = Order.builder()
                .orderId(orderId)
                .symbol("AAPL")
                .side(OrderSide.BUY)
                .quantity(200L)
                .price(BigDecimal.valueOf(160.0))
                .type(OrderType.LIMIT)
                .status(OrderStatus.NEW)
                .clientOrderId("CLIENT123")
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(modifiedOrder);

        // When
        Order result = orderService.modifyOrder(request);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(200L, result.getQuantity());
        assertEquals(BigDecimal.valueOf(160.0), result.getPrice());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void cancelOrder_ValidRequest_ReturnsCancelledOrder() {
        // Given
        String orderId = "ORDER123";
        OrderRequest request = OrderRequest.builder()
                .orderId(orderId)
                .build();

        Order existingOrder = Order.builder()
                .orderId(orderId)
                .symbol("AAPL")
                .side(OrderSide.BUY)
                .quantity(100L)
                .price(BigDecimal.valueOf(150.0))
                .type(OrderType.LIMIT)
                .status(OrderStatus.NEW)
                .clientOrderId("CLIENT123")
                .build();

        Order cancelledOrder = Order.builder()
                .orderId(orderId)
                .symbol("AAPL")
                .side(OrderSide.BUY)
                .quantity(100L)
                .price(BigDecimal.valueOf(150.0))
                .type(OrderType.LIMIT)
                .status(OrderStatus.CANCELLED)
                .clientOrderId("CLIENT123")
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(cancelledOrder);

        // When
        Order result = orderService.cancelOrder(request);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(OrderStatus.CANCELLED, result.getStatus());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }
}