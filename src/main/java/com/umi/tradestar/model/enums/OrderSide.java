package com.umi.tradestar.model.enums;

/**
 * Enum representing the side of an order (Buy or Sell).
 *
 * @author VrushankPatel
 */
public enum OrderSide {
    BUY,
    SELL;

    public static OrderSide fromFixSide(char fixSide) {
        return fixSide == '1' ? BUY : SELL;
    }

    public static OrderSide fromItchSide(char itchSide) {
        return itchSide == 'B' ? BUY : SELL;
    }

    public static OrderSide fromOuchSide(char ouchSide) {
        return ouchSide == 'B' ? BUY : SELL;
    }

    public char toFixSide() {
        return this == BUY ? '1' : '2';
    }

    public char toItchSide() {
        return this == BUY ? 'B' : 'S';
    }

    public char toOuchSide() {
        return this == BUY ? 'B' : 'S';
    }
}