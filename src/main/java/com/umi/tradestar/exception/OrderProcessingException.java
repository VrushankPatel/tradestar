package com.umi.tradestar.exception;

public class OrderProcessingException extends TradestarBusinessException {
    public static final String ERROR_CODE_INSUFFICIENT_FUNDS = "ORD001";
    public static final String ERROR_CODE_INVALID_ORDER_STATUS = "ORD002";
    public static final String ERROR_CODE_MARKET_CLOSED = "ORD003";
    public static final String ERROR_CODE_INVALID_QUANTITY = "ORD004";
    public static final String ERROR_CODE_ORDER_NOT_FOUND = "ORD005";

    public OrderProcessingException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public OrderProcessingException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public static OrderProcessingException insufficientFunds(double available, double required) {
        return new OrderProcessingException(ERROR_CODE_INSUFFICIENT_FUNDS,
            String.format("Insufficient funds. Available: %.2f, Required: %.2f", available, required));
    }

    public static OrderProcessingException invalidOrderStatus(String currentStatus, String expectedStatus) {
        return new OrderProcessingException(ERROR_CODE_INVALID_ORDER_STATUS,
            String.format("Invalid order status. Current: %s, Expected: %s", currentStatus, expectedStatus));
    }

    public static OrderProcessingException marketClosed() {
        return new OrderProcessingException(ERROR_CODE_MARKET_CLOSED,
            "Market is currently closed");
    }

    public static OrderProcessingException invalidQuantity(String reason) {
        return new OrderProcessingException(ERROR_CODE_INVALID_QUANTITY,
            String.format("Invalid order quantity: %s", reason));
    }

    public static OrderProcessingException orderNotFound(String orderId) {
        return new OrderProcessingException(ERROR_CODE_ORDER_NOT_FOUND,
            String.format("Order with ID %s not found", orderId));
    }
}