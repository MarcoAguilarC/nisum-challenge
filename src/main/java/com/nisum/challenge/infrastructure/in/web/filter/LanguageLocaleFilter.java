package com.nisum.challenge.infrastructure.in.web.filter;

import com.nisum.challenge.application.util.RequestLocaleHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class LanguageLocaleFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LanguageLocaleFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Locale previous = LocaleContextHolder.getLocale();
        try {
            Locale resolved = resolveLocale(request);
            if (resolved != null) {
                LocaleContextHolder.setLocale(resolved);
                RequestLocaleHolder.set(resolved);
            }
            filterChain.doFilter(request, response);
        } finally {
            LocaleContextHolder.setLocale(previous);
            RequestLocaleHolder.clear();
        }
    }

    private Locale resolveLocale(HttpServletRequest request) {
        Locale loc = toSupportedLocale(request.getParameter("lang"));
        if (loc != null) return loc;

        Locale accept = request.getLocale();
        loc = toSupportedLocale(accept != null ? accept.getLanguage() : null);
        if (loc != null) return loc;

        return null;
    }


    private Locale toSupportedLocale(String lang) {
        if (lang == null || lang.isEmpty()) return null;
        String norm = lang.toLowerCase(Locale.ROOT);
        switch (norm) {
            case "es":
                return new Locale("es");
            case "en":
                return Locale.ENGLISH;
            default:
                return null;
        }
    }
}
