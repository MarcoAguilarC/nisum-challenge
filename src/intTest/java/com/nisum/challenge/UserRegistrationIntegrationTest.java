package com.nisum.challenge;

import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.domain.model.User;
import com.nisum.challenge.infrastructure.out.persistence.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserJpaRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_Success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Integration Test User");
        userRequest.setEmail("integration.test@example.com");
        userRequest.setPassword("Password123@");
        userRequest.setPhones(Collections.emptyList());

        ResponseEntity<UserResponse> response = restTemplate.postForEntity("/users/register", userRequest, UserResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getToken());
        assertTrue(response.getBody().isActive());

        Optional<User> savedUser = userRepository.findByEmail("integration.test@example.com");
        assertTrue(savedUser.isPresent());
        assertEquals("Integration Test User", savedUser.get().getName());
    }

    @Test
    void registerUser_EmailAlreadyExists_ReturnsBadRequest() {
        User existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("password");
        userRepository.save(existingUser);

        UserRequest userRequest = new UserRequest();
        userRequest.setName("New User");
        userRequest.setEmail("existing@example.com");
        userRequest.setPassword("Password123@");

        ResponseEntity<UserResponse> response = restTemplate.postForEntity("/users/register", userRequest, UserResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
