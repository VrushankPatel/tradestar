package com.umi.tradestar.itch;

import lombok.Data;

@Data
public class ItchOrderCancel implements ItchMessage {
    private final char messageType = 'X';
    private final long orderReferenceNumber;
    private final long shares;
} 