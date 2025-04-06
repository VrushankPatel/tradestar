package com.umi.tradestar.exception;

public class AuthenticationException extends TradestarBusinessException {
    public static final String ERROR_CODE_INVALID_CREDENTIALS = "AUTH001";
    public static final String ERROR_CODE_TOKEN_EXPIRED = "AUTH002";
    public static final String ERROR_CODE_TOKEN_INVALID = "AUTH003";
    public static final String ERROR_CODE_USER_NOT_FOUND = "AUTH004";
    public static final String ERROR_CODE_USER_DISABLED = "AUTH005";

    public AuthenticationException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public AuthenticationException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException(ERROR_CODE_INVALID_CREDENTIALS, "Invalid username or password");
    }

    public static AuthenticationException tokenExpired() {
        return new AuthenticationException(ERROR_CODE_TOKEN_EXPIRED, "Authentication token has expired");
    }

    public static AuthenticationException tokenInvalid() {
        return new AuthenticationException(ERROR_CODE_TOKEN_INVALID, "Invalid authentication token");
    }

    public static AuthenticationException userNotFound() {
        return new AuthenticationException(ERROR_CODE_USER_NOT_FOUND, "User not found");
    }
    
    public static AuthenticationException userDisabled() {
        return new AuthenticationException(ERROR_CODE_USER_DISABLED, "User account is disabled");
    }
}