package com.nisum.challenge.infrastructure.in.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackIdFilterTest {

    private TrackIdFilter trackIdFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        trackIdFilter = new TrackIdFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void doFilterInternal_NoCorrelationIdHeader_GeneratesNewId() throws ServletException, IOException {
        doAnswer(invocation -> {
            String correlationId = MDC.get("correlationId");
            assertNotNull(correlationId, "Correlation ID should be in MDC during filter execution");
            assertDoesNotThrow(() -> UUID.fromString(correlationId));
            return null;
        }).when(filterChain).doFilter(request, response);

        trackIdFilter.doFilterInternal(request, response, filterChain);

        assertNull(MDC.get("correlationId"), "MDC should be clean after filter execution");
        assertNotNull(response.getHeader("X-Correlation-ID"), "Response header should be set");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithCorrelationIdHeader_UsesExistingId() throws ServletException, IOException {
        String existingCorrelationId = UUID.randomUUID().toString();
        request.addHeader("X-Correlation-ID", existingCorrelationId);

        doAnswer(invocation -> {
            assertEquals(existingCorrelationId, MDC.get("correlationId"), "MDC should use existing ID");
            return null;
        }).when(filterChain).doFilter(request, response);

        trackIdFilter.doFilterInternal(request, response, filterChain);

        assertNull(MDC.get("correlationId"), "MDC should be clean after filter execution");
        assertEquals(existingCorrelationId, response.getHeader("X-Correlation-ID"));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_EmptyCorrelationIdHeader_GeneratesNewId() throws ServletException, IOException {
        request.addHeader("X-Correlation-ID", "");

        doAnswer(invocation -> {
            String correlationId = MDC.get("correlationId");
            assertNotNull(correlationId, "New ID should be generated for empty header");
            assertFalse(correlationId.isEmpty());
            return null;
        }).when(filterChain).doFilter(request, response);

        trackIdFilter.doFilterInternal(request, response, filterChain);

        assertNull(MDC.get("correlationId"), "MDC should be clean after filter execution");
        String headerValue = response.getHeader("X-Correlation-ID");
        assertNotNull(headerValue);
        assertFalse(headerValue.isEmpty());
        verify(filterChain).doFilter(request, response);
    }
}
