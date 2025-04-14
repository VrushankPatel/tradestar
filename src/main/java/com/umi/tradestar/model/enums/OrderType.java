package com.umi.tradestar.model.enums;

/**
 * Enum representing the type of order.
 * - MARKET: Order executed at the current market price
 * - LIMIT: Order executed at specified price or better
 *
 * @author VrushankPatel
 */
public enum OrderType {
    MARKET,
    LIMIT;

    public static OrderType fromFixOrdType(char fixOrdType) {
        return fixOrdType == '1' ? MARKET : LIMIT;
    }

    public static OrderType fromOuchType(char ouchType) {
        return ouchType == 'M' ? MARKET : LIMIT;
    }

    public char toFixOrdType() {
        return this == MARKET ? '1' : '2';
    }

    public char toOuchType() {
        return this == MARKET ? 'M' : 'L';
    }
}