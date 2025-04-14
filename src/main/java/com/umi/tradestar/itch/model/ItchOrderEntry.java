package com.umi.tradestar.itch.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.umi.tradestar.model.OrderType;
import com.umi.tradestar.model.Side;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderEntry extends ItchMessage {
    private String symbol;
    private Side side;
    private long quantity;
    private double price;
    private OrderType orderType;
    private String orderReference;
} 