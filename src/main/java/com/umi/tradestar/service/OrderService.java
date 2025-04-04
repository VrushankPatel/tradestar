package com.umi.tradestar.service;

import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.OrderStatus;
import com.umi.tradestar.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class for handling order-related operations in the trading system.
 * Manages order creation, validation, and lifecycle.
 *
 * @author VrushankPatel
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Order order) {
        validateOrder(order);
        
        // Set initial order properties
        order.setOrderId(generateOrderId());
        order.setStatus(OrderStatus.NEW);
        order.setFilledQuantity(BigDecimal.ZERO);
        order.setAveragePrice(BigDecimal.ZERO);
        order.setTrader(getCurrentUser());
        
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByTrader() {
        return orderRepository.findByTrader(getCurrentUser());
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        validateOrderCancellation(order);
        
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    private void validateOrder(Order order) {
        if (order.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Order quantity must be positive");
        }
        
        if (order.getSymbol() == null || order.getSymbol().trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol is required");
        }
        
        // Add more validation rules as needed
    }

    private void validateOrderCancellation(Order order) {
        if (order.getStatus() == OrderStatus.FILLED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel order in " + order.getStatus() + " state");
        }
        
        if (!order.getTrader().equals(getCurrentUser())) {
            throw new IllegalStateException("Not authorized to cancel this order");
        }
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}