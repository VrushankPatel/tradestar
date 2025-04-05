package com.umi.tradestar.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TradestarBusinessException.class)
    public ResponseEntity<Object> handleTradestarBusinessException(TradestarBusinessException ex, WebRequest request) {
        logger.error("Business exception occurred:", ex);
        return createErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.error("Authentication exception occurred:", ex);
        return createErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        logger.error("Validation exception occurred:", ex);
        return createErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderProcessingException.class)
    public ResponseEntity<Object> handleOrderProcessingException(OrderProcessingException ex, WebRequest request) {
        logger.error("Order processing exception occurred:", ex);
        return createErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred:", ex);
        return createErrorResponse("INTERNAL_ERROR", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> createErrorResponse(String errorCode, String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("errorCode", errorCode);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}