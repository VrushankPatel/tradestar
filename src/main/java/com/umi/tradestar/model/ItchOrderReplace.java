package com.umi.tradestar.model;

import com.umi.tradestar.model.enums.OrderType;
import com.umi.tradestar.model.enums.Side;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderReplace {
    private String originalOrderReference;
    private String newOrderReference;
    private String symbol;
    private Side side;
    private long quantity;
    private double price;
    private OrderType orderType;
} 