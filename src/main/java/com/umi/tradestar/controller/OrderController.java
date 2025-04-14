package com.umi.tradestar.controller;

import com.umi.tradestar.dto.OrderRequest;
import com.umi.tradestar.dto.OrderResponse;
import com.umi.tradestar.model.Order;
import com.umi.tradestar.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing trading orders.
 * Provides endpoints for order creation, retrieval, and cancellation.
 *
 * @author VrushankPatel
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new trading order for the authenticated user")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(convertToResponse(order));
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Modify order", description = "Modifies an existing trading order")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderResponse> modifyOrder(@PathVariable String orderId, @RequestBody OrderRequest request) {
        request.setOrderId(orderId);
        Order order = orderService.modifyOrder(request);
        return ResponseEntity.ok(convertToResponse(order));
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels a pending order")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        OrderRequest request = new OrderRequest();
        request.setOrderId(orderId);
        Order order = orderService.cancelOrder(request);
        return ResponseEntity.ok(convertToResponse(order));
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order", description = "Deletes an existing trading order")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable String orderId) {
        OrderRequest request = new OrderRequest();
        request.setOrderId(orderId);
        Order order = orderService.deleteOrder(request);
        return ResponseEntity.ok(convertToResponse(order));
    }

    @PostMapping("/{orderId}/replace")
    @Operation(summary = "Replace order", description = "Replaces an existing trading order with a new one")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderResponse> replaceOrder(@PathVariable String orderId, @RequestBody OrderRequest request) {
        request.setOrderId(orderId);
        Order order = orderService.replaceOrder(request);
        return ResponseEntity.ok(convertToResponse(order));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Retrieves a specific order by its ID")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(convertToResponse(order));
    }

    @GetMapping
    @Operation(summary = "Get user's orders", description = "Retrieves all orders for the authenticated user")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
    }

    private OrderResponse convertToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getOrderId())
                .symbol(order.getSymbol())
                .side(order.getSide())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .type(order.getType())
                .status(order.getStatus())
                .clientOrderId(order.getClientOrderId())
                .filledQuantity(order.getFilledQuantity())
                .averagePrice(order.getAveragePrice())
                .build();
    }
}