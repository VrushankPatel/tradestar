package com.umi.tradestar.dto;

import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderStatus;
import com.umi.tradestar.model.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for order responses
 * 
 * @author VrushankPatel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String symbol;
    private OrderSide side;
    private Long quantity;
    private BigDecimal price;
    private OrderType type;
    private OrderStatus status;
    private String clientOrderId;
    private Long filledQuantity;
    private BigDecimal averagePrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String fixOrderId;
    private String ouchOrderId;
    private String itchOrderId;
} 