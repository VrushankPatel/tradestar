package com.umi.tradestar.dto;

import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderStatus;
import com.umi.tradestar.model.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order creation requests
 * 
 * @author VrushankPatel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String orderId;
    private String symbol;
    private OrderSide side;
    private Long quantity;
    private BigDecimal price;
    private OrderType type;
    private OrderStatus status;
    private String clientOrderId;
    private Long filledQuantity;
    private BigDecimal averagePrice;
} 