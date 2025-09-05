package com.nisum.challenge.infrastructure.in.web.exception;

import com.nisum.challenge.application.util.MessageService;
import com.nisum.challenge.domain.exception.PhoneAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void handleRuntimeException_invalidCredentials_returnsUnauthorized() {
        String errorMessageKey = "error.credentials.invalid";
        RuntimeException ex = new RuntimeException(errorMessageKey);

        when(messageService.getMessage(eq(errorMessageKey))).thenReturn("Invalid credentials message");
        when(messageService.getMessage(eq("log.error.unexpected.runtime"))).thenReturn("Log: Unexpected runtime");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid credentials message", response.getBody().get("message"));
    }

    @Test
    void handleRuntimeException_otherRuntimeException_returnsInternalServerError() {
        String errorMessage = "Some other runtime error";
        RuntimeException ex = new RuntimeException(errorMessage);

        when(messageService.getMessage(eq(errorMessage))).thenReturn(errorMessage);
        when(messageService.getMessage(eq("log.error.unexpected.runtime"))).thenReturn("Log: Unexpected runtime");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    void handleIllegalArgumentException_returnsBadRequest() {
        String errorMessage = "Illegal argument";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);

        when(messageService.getMessage(eq(errorMessage))).thenReturn(errorMessage);
        when(messageService.getMessage(eq("log.warn.bad.argument"), any(Object[].class))).thenReturn("Log: Bad argument");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    void handlePhoneAlreadyExistException_returnsConflict() {
        String messageKey = "error.phone.exists";
        Object[] args = new Object[]{"123456789"};
        PhoneAlreadyExistException ex = new PhoneAlreadyExistException(messageKey, args);

        when(messageService.getMessage(eq(messageKey), eq(args))).thenReturn("The phone 123456789 is already registered.");
        when(messageService.getMessage(eq("log.warn.phone.exists"), any(Object[].class))).thenReturn("Log: Phone exists");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handlePhoneNumberAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("The phone 123456789 is already registered.", response.getBody().get("message"));
    }

    @Test
    void handleValidationExceptions_returnsBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = mock(FieldError.class);
        when(fieldError1.getDefaultMessage()).thenReturn("Error message 1");
        FieldError fieldError2 = mock(FieldError.class);
        when(fieldError2.getDefaultMessage()).thenReturn("Error message 2");

        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Validation failed message");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error message 1, Error message 2", response.getBody().get("message"));
    }

    @Test
    void handleValidationExceptions_noErrors_returnsBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Validation failed message");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().get("message"));
    }
}
