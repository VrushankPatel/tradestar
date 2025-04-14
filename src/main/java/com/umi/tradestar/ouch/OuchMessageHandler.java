package com.umi.tradestar.ouch;

import com.umi.tradestar.dto.OrderRequest;
import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderType;
import com.umi.tradestar.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Handler for OUCH protocol messages
 * @author VrushankPatel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OuchMessageHandler {
    private final OrderService orderService;

    public void handleEnterOrder(EnterOrder message) {
        log.info("Processing OUCH order: {}", message);
        try {
            OrderRequest orderRequest = OrderRequest.builder()
                    .symbol(message.getSymbol())
                    .side(OrderSide.fromOuchSide(message.getSide()))
                    .quantity(message.getQuantity())
                    .price(BigDecimal.valueOf(message.getPrice()))
                    .type(OrderType.fromOuchType(message.getOrderType()))
                    .clientOrderId(message.getClientOrderId())
                    .build();

            Order order = orderService.createOrder(orderRequest);
            log.info("Successfully created order from OUCH message: {}", message.getClientOrderId());
        } catch (Exception e) {
            log.error("Error processing OUCH order: {}", e.getMessage(), e);
        }
    }
} 