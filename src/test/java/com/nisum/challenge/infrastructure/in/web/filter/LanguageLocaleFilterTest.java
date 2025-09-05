package com.nisum.challenge.infrastructure.in.web.filter;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import jakarta.servlet.FilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageLocaleFilterTest {

    private final LanguageLocaleFilter filter = new LanguageLocaleFilter();

    @AfterEach
    void cleanup() {
        LocaleContextHolder.resetLocaleContext();
    }


    @Test
    void acceptLanguage_used_when_xLanguage_missing() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "en-GB,en;q=0.8");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> assertEquals("en", LocaleContextHolder.getLocale().getLanguage());

        filter.doFilter(request, response, chain);
    }

    @Test
    void unsupported_values_are_ignored_and_default_kept() throws ServletException, IOException {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "fr-FR,fr;q=0.9");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> assertEquals("en", LocaleContextHolder.getLocale().getLanguage());

        filter.doFilter(request, response, chain);
    }
}
