package com.umi.tradestar.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        logger.error("Bad credentials:", ex);
        return createErrorResponse("AUTH001", "Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleDisabledException(DisabledException ex, WebRequest request) {
        logger.error("User account is disabled:", ex);
        return createErrorResponse("AUTH005", "User account is disabled", HttpStatus.FORBIDDEN);
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

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(org.springframework.web.HttpRequestMethodNotSupportedException ex, WebRequest request) {
        logger.error("Method not supported:", ex);
        // return createErrorResponse(
        //     "METHOD_NOT_SUPPORTED",
        //     String.format("The %s method is not supported for this request. Supported methods are: %s",
        //             ex.getMethod(), String.join(", ", ex.getSupportedHttpMethods())),
        //     HttpStatus.METHOD_NOT_ALLOWED
        // );

        return createErrorResponse(
    "METHOD_NOT_SUPPORTED",
    String.format("The %s method is not supported for this request. Supported methods are: %s",
        ex.getMethod(),
        ex.getSupportedHttpMethods().stream()
            .map(HttpMethod::name) // or .toString()
            .collect(Collectors.joining(", "))
    ),
    HttpStatus.METHOD_NOT_ALLOWED
);

        

    }

    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<Object> handleJwtException(io.jsonwebtoken.JwtException ex, WebRequest request) {
        logger.error("JWT exception occurred:", ex);
        String message = "Authentication token error: " + ex.getMessage();
        return createErrorResponse("AUTH006", message, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(io.jsonwebtoken.security.WeakKeyException.class)
    public ResponseEntity<Object> handleWeakKeyException(io.jsonwebtoken.security.WeakKeyException ex, WebRequest request) {
        logger.error("JWT weak key exception occurred:", ex);
        return createErrorResponse("AUTH007", "Server authentication configuration error", HttpStatus.INTERNAL_SERVER_ERROR);
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