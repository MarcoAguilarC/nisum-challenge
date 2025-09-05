package com.nisum.challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class H2ConsoleIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void h2Console_isAccessible() {
        ResponseEntity<String> response = restTemplate.getForEntity("/h2-console/", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
