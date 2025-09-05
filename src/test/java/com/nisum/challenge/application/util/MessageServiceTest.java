package com.nisum.challenge.application.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @Test
    void getMessage_withKey_returnsMessage() {
        String key = "test.key";
        String expectedMessage = "Test Message";
        when(messageSource.getMessage(eq(key), any(), eq(key), any(Locale.class))).thenReturn(expectedMessage);

        String actualMessage = messageService.getMessage(key);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getMessage_withKeyAndArgs_returnsFormattedMessage() {
        String key = "test.key.args";
        Object[] args = new Object[]{"arg1"};
        String expectedMessage = "Test Message with arg1";
        when(messageSource.getMessage(eq(key), eq(args), eq(key), any(Locale.class))).thenReturn(expectedMessage);

        String actualMessage = messageService.getMessage(key, args);

        assertEquals(expectedMessage, actualMessage);
    }
}
