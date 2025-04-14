package com.umi.tradestar.service;

import com.umi.tradestar.dto.OrderRequest;
import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderStatus;
import com.umi.tradestar.model.enums.OrderType;
import com.umi.tradestar.repository.OrderRepository;
import com.umi.tradestar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service class for handling order-related operations in the trading system.
 * Manages order creation, validation, and lifecycle.
 *
 * @author VrushankPatel
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public Order createOrder(OrderRequest request) {
        log.info("Creating new order: {}", request);
        validateOrderRequest(request);
        
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .symbol(request.getSymbol())
                .side(request.getSide())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .type(request.getType())
                .status(OrderStatus.NEW)
                .clientOrderId(request.getClientOrderId())
                .build();

        return orderRepository.save(order);
    }

    @Transactional
    public Order modifyOrder(OrderRequest request) {
        log.info("Modifying order: {}", request);
        validateOrderModification(request);
        
        Order order = getOrderById(request.getOrderId());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(OrderRequest request) {
        log.info("Canceling order: {}", request);
        validateOrderCancellation(request);
        
        Order order = getOrderById(request.getOrderId());
        order.setStatus(OrderStatus.CANCELLED);
        
        return orderRepository.save(order);
    }

    @Transactional
    public Order deleteOrder(OrderRequest request) {
        log.info("Deleting order: {}", request);
        validateOrderDeletion(request);
        
        Order order = getOrderById(request.getOrderId());
        orderRepository.delete(order);
        return order;
    }

    @Transactional
    public Order replaceOrder(OrderRequest request) {
        log.info("Replacing order: {}", request);
        validateOrderReplacement(request);
        
        Order oldOrder = getOrderById(request.getOrderId());
        oldOrder.setStatus(OrderStatus.REPLACED);
        orderRepository.save(oldOrder);
        
        Order newOrder = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .symbol(oldOrder.getSymbol())
                .side(oldOrder.getSide())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .type(oldOrder.getType())
                .status(OrderStatus.NEW)
                .clientOrderId(request.getClientOrderId())
                .build();
        
        return orderRepository.save(newOrder);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersBySymbol(String symbol) {
        return orderRepository.findBySymbol(symbol);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersBySide(OrderSide side) {
        return orderRepository.findBySide(side);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void validateOrderRequest(OrderRequest request) {
        if (request.getSymbol() == null || request.getSymbol().isEmpty()) {
            throw new IllegalArgumentException("Symbol is required");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (request.getSide() == null) {
            throw new IllegalArgumentException("Order side is required");
        }
        if (request.getType() == null) {
            throw new IllegalArgumentException("Order type is required");
        }
    }

    private void validateOrderModification(OrderRequest request) {
        if (request.getOrderId() == null || request.getOrderId().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }

    private void validateOrderCancellation(OrderRequest request) {
        if (request.getOrderId() == null || request.getOrderId().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
    }

    private void validateOrderDeletion(OrderRequest request) {
        if (request.getOrderId() == null || request.getOrderId().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
    }

    private void validateOrderReplacement(OrderRequest request) {
        if (request.getOrderId() == null || request.getOrderId().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}