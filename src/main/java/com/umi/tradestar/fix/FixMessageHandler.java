package com.umi.tradestar.fix;

import com.umi.tradestar.dto.OrderRequest;
import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderType;
import com.umi.tradestar.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.*;

import java.math.BigDecimal;

/**
 * Handler for FIX messages
 * @author VrushankPatel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FixMessageHandler {
    private final OrderService orderService;

    /**
     * Handle incoming FIX message
     * @param message The FIX message
     * @param sessionID The FIX session ID
     */
    public void handleMessage(Message message, SessionID sessionID) {
        if (message instanceof quickfix.fix42.NewOrderSingle) {
            handleNewOrderSingle(message, sessionID);
        }
    }

    /**
     * Handle NewOrderSingle message
     * @param message The NewOrderSingle message
     * @param sessionID The FIX session ID
     */
    public void handleNewOrderSingle(Message message, SessionID sessionID) {
        log.info("Processing FIX order: {}", message);
        try {
            String symbol = message.getString(Symbol.FIELD);
            char side = message.getChar(Side.FIELD);
            double quantity = message.getDouble(OrderQty.FIELD);
            double price = message.getDouble(Price.FIELD);
            char ordType = message.getChar(OrdType.FIELD);
            String clientOrderId = message.getString(ClOrdID.FIELD);

            OrderRequest orderRequest = OrderRequest.builder()
                    .symbol(symbol)
                    .side(OrderSide.fromFixSide(side))
                    .quantity((long) quantity)
                    .price(BigDecimal.valueOf(price))
                    .type(OrderType.fromFixOrdType(ordType))
                    .clientOrderId(clientOrderId)
                    .build();

            Order order = orderService.createOrder(orderRequest);
            log.info("Successfully created order from FIX message: {}", clientOrderId);
        } catch (Exception e) {
            log.error("Error processing FIX order: {}", e.getMessage(), e);
        }
    }
} 