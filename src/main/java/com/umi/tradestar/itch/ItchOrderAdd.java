package com.umi.tradestar.itch;

import lombok.Data;

@Data
public class ItchOrderAdd implements ItchMessage {
    private final char messageType = 'A';
    private final String stock;
    private final char side;
    private final long shares;
    private final double price;
    private final long orderReferenceNumber;
} 