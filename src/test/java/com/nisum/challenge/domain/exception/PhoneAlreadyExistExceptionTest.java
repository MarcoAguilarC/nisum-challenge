package com.nisum.challenge.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PhoneAlreadyExistExceptionTest {

    @Test
    void exceptionIsCreatedWithMessageAndArgs() {
        String messageKey = "error.phone.exists";
        Object[] args = new Object[]{"123456789"};

        PhoneAlreadyExistException exception = new PhoneAlreadyExistException(messageKey, args);

        assertEquals(messageKey, exception.getMessage());
        assertArrayEquals(args, exception.getArgs());
    }
}
