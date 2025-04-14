package com.umi.tradestar.ouch;

import lombok.Data;

@Data
public class EnterOrder {
    private final String symbol;
    private final char side;
    private final long quantity;
    private final double price;
    private final char orderType;
    private final String clientOrderId;
} 