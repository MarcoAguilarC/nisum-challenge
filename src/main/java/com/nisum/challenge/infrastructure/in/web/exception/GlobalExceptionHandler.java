package com.nisum.challenge.infrastructure.in.web.exception;

import com.nisum.challenge.application.util.MessageService;
import com.nisum.challenge.domain.exception.PhoneAlreadyExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageService messageService;

    public GlobalExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = messageService.getMessage(ex.getMessage());
        logger.warn(messageService.getMessage("log.warn.bad.argument", new Object[]{message}));
        Map<String, String> errorResponse = Collections.singletonMap("message", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn(messageService.getMessage("log.warn.validation.failed", new Object[]{ex.getMessage()}));
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        Map<String, String> errorResponse = Collections.singletonMap("message", errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String message = messageService.getMessage(ex.getMessage());
        logger.error(messageService.getMessage("log.error.unexpected.runtime"), ex);
        Map<String, String> errorResponse = Collections.singletonMap("message", message);
        if ("error.credentials.invalid".equals(ex.getMessage())) {
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PhoneAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handlePhoneNumberAlreadyExistsException(PhoneAlreadyExistException ex) {
        String message = messageService.getMessage(ex.getMessage(), ex.getArgs());
        logger.warn(messageService.getMessage("log.warn.phone.exists", new Object[]{message}));
        Map<String, String> errorResponse = Collections.singletonMap("message", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
