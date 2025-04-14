package com.umi.tradestar.itch;

import lombok.Data;

@Data
public class ItchOrderDelete implements ItchMessage {
    private final char messageType = 'D';
    private final long orderReferenceNumber;
} 