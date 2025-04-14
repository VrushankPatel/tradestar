package com.umi.tradestar.itch;

import lombok.Data;

@Data
public class ItchOrderModify implements ItchMessage {
    private final char messageType = 'E';
    private final long orderReferenceNumber;
    private final long shares;
    private final double price;
} 