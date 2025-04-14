package com.umi.tradestar.itch;

import com.umi.tradestar.dto.OrderRequest;
import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderType;
import com.umi.tradestar.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Handler for ITCH protocol messages
 * @author VrushankPatel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ItchMessageHandler {
    private final OrderService orderService;

    public void processMessage(ItchMessage message) {
        switch (message.getMessageType()) {
            case 'A' -> processOrderAdd((ItchOrderAdd) message);
            case 'E' -> processOrderModify((ItchOrderModify) message);
            case 'X' -> processOrderCancel((ItchOrderCancel) message);
            case 'D' -> processOrderDelete((ItchOrderDelete) message);
            case 'U' -> processOrderReplace((ItchOrderReplace) message);
            default -> log.warn("Unknown message type: {}", message.getMessageType());
        }
    }

    private void processOrderAdd(ItchOrderAdd message) {
        log.info("Processing ITCH order add: {}", message);
        try {
            OrderRequest orderRequest = OrderRequest.builder()
                    .symbol(message.getStock())
                    .side(OrderSide.fromItchSide(message.getSide()))
                    .quantity(message.getShares())
                    .price(BigDecimal.valueOf(message.getPrice()))
                    .type(OrderType.LIMIT)
                    .clientOrderId(String.valueOf(message.getOrderReferenceNumber()))
                    .build();

            orderService.createOrder(orderRequest);
            log.info("Successfully created order from ITCH add: {}", message.getOrderReferenceNumber());
        } catch (Exception e) {
            log.error("Error processing ITCH order add: {}", e.getMessage(), e);
        }
    }

    private void processOrderModify(ItchOrderModify message) {
        log.info("Processing ITCH order modify: {}", message);
        try {
            OrderRequest orderRequest = OrderRequest.builder()
                    .orderId(String.valueOf(message.getOrderReferenceNumber()))
                    .quantity(message.getShares())
                    .price(BigDecimal.valueOf(message.getPrice()))
                    .build();

            orderService.modifyOrder(orderRequest);
            log.info("Successfully modified order from ITCH modify: {}", message.getOrderReferenceNumber());
        } catch (Exception e) {
            log.error("Error processing ITCH order modify: {}", e.getMessage(), e);
        }
    }

    private void processOrderCancel(ItchOrderCancel message) {
        log.info("Processing ITCH order cancel: {}", message);
        try {
            OrderRequest orderRequest = OrderRequest.builder()
                    .orderId(String.valueOf(message.getOrderReferenceNumber()))
                    .quantity(message.getShares())
                    .build();

            orderService.cancelOrder(orderRequest);
            log.info("Successfully canceled order from ITCH cancel: {}", message.getOrderReferenceNumber());
        } catch (Exception e) {
            log.error("Error processing ITCH order cancel: {}", e.getMessage(), e);
        }
    }

    private void processOrderDelete(ItchOrderDelete message) {
        log.info("Processing ITCH order delete: {}", message);
        try {
            OrderRequest orderRequest = OrderRequest.builder()
                    .orderId(String.valueOf(message.getOrderReferenceNumber()))
                    .build();

            orderService.deleteOrder(orderRequest);
            log.info("Successfully deleted order from ITCH delete: {}", message.getOrderReferenceNumber());
        } catch (Exception e) {
            log.error("Error processing ITCH order delete: {}", e.getMessage(), e);
        }
    }

    private void processOrderReplace(ItchOrderReplace message) {
        log.info("Processing ITCH order replace: {}", message);
        try {
            OrderRequest orderRequest = OrderRequest.builder()
                    .orderId(String.valueOf(message.getOriginalOrderReferenceNumber()))
                    .quantity(message.getShares())
                    .price(BigDecimal.valueOf(message.getPrice()))
                    .clientOrderId(String.valueOf(message.getNewOrderReferenceNumber()))
                    .build();

            orderService.replaceOrder(orderRequest);
            log.info("Successfully replaced order from ITCH replace: {} -> {}", 
                    message.getOriginalOrderReferenceNumber(), 
                    message.getNewOrderReferenceNumber());
        } catch (Exception e) {
            log.error("Error processing ITCH order replace: {}", e.getMessage(), e);
        }
    }
} 