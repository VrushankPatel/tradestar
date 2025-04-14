package com.umi.tradestar.itch;

import lombok.Data;

@Data
public class ItchOrderReplace implements ItchMessage {
    private final char messageType = 'U';
    private final long originalOrderReferenceNumber;
    private final long newOrderReferenceNumber;
    private final long shares;
    private final double price;
} 