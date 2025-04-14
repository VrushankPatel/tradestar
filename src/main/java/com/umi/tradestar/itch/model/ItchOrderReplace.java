package com.umi.tradestar.itch.model;

import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderReplace extends ItchMessage {
    private String symbol;
    private OrderSide side;
    private Long quantity;
    private BigDecimal price;
    private OrderType orderType;
    private String orderReference;
    private String originalOrderReference;
} 