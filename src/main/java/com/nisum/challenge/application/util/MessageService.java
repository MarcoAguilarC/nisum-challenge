package com.nisum.challenge.application.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key) {
        return getMessage(key, null);
    }

    public String getMessage(String key, Object[] args) {
        Locale locale = RequestLocaleHolder.get();
        if (locale == null) {
            locale = LocaleContextHolder.getLocale();
        }
        return messageSource.getMessage(key, args, key, locale);
    }
}
