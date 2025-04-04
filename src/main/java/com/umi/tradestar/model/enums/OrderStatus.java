package com.umi.tradestar.model.enums;

/**
 * Enum representing the possible states of an order in the trading system.
 *
 * @author VrushankPatel
 */
public enum OrderStatus {
    NEW,             // Order has been accepted but not yet processed
    PARTIALLY_FILLED, // Order has been partially filled
    FILLED,          // Order has been completely filled
    CANCELLED,       // Order has been cancelled
    REJECTED,        // Order has been rejected
    EXPIRED,         // Order has expired
    PENDING_CANCEL,  // Cancellation requested but not confirmed
    PENDING_REPLACE  // Modification requested but not confirmed
}