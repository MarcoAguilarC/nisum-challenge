package com.nisum.challenge.infrastructure.in.web.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedirectControllerTest {

    @Test
    void redirectToSwaggerUi_returnsCorrectRedirectUrl() {
        RedirectController controller = new RedirectController();
        String result = controller.redirectToSwaggerUi();
        assertEquals("redirect:/swagger-ui.html", result);
    }
}
