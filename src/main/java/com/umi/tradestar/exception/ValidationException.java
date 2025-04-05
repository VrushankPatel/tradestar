package com.umi.tradestar.exception;

public class ValidationException extends TradestarBusinessException {
    public static final String ERROR_CODE_INVALID_INPUT = "VAL001";
    public static final String ERROR_CODE_MISSING_REQUIRED_FIELD = "VAL002";
    public static final String ERROR_CODE_INVALID_FORMAT = "VAL003";
    public static final String ERROR_CODE_INVALID_STATE = "VAL004";

    public ValidationException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public ValidationException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public static ValidationException invalidInput(String field, String reason) {
        return new ValidationException(ERROR_CODE_INVALID_INPUT, 
            String.format("Invalid input for field '%s': %s", field, reason));
    }

    public static ValidationException missingRequiredField(String fieldName) {
        return new ValidationException(ERROR_CODE_MISSING_REQUIRED_FIELD,
            String.format("Required field '%s' is missing", fieldName));
    }

    public static ValidationException invalidFormat(String field, String expectedFormat) {
        return new ValidationException(ERROR_CODE_INVALID_FORMAT,
            String.format("Invalid format for field '%s'. Expected format: %s", field, expectedFormat));
    }

    public static ValidationException invalidState(String message) {
        return new ValidationException(ERROR_CODE_INVALID_STATE, message);
    }
}