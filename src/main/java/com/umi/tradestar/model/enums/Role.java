package com.umi.tradestar.model.enums;

/**
 * Enum representing the available roles in the system.
 * - TRADER: Can send orders and interact with trading functionality
 * - ADMIN: Has full access to system features and administration
 * - OBSERVER: Can only view market data and order status
 *
 * @author VrushankPatel
 */
public enum Role {
    TRADER,
    ADMIN,
    OBSERVER
}