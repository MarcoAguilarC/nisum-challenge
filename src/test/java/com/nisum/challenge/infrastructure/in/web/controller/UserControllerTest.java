package com.nisum.challenge.infrastructure.in.web.controller;

import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.application.port.in.UserUseCase;
import com.nisum.challenge.application.util.MessageService;
import com.nisum.challenge.domain.exception.PhoneAlreadyExistException;
import com.nisum.challenge.infrastructure.in.web.exception.GlobalExceptionHandler;
import com.nisum.challenge.testutil.TestResourceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UserController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserUseCase userUseCase;
    @MockBean private MessageService messageService;

    @BeforeEach
    void setUp() {
        when(messageService.getMessage(anyString(), any())).thenReturn("some message");
    }

    @Test
    void registerUser_ReturnsCreated_withBody() throws Exception {
        UserResponse resp = UserResponse.builder()
                .id(UUID.randomUUID())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("JWT-123")
                .isActive(true)
                .build();

        when(userUseCase.registerUser(any(UserRequest.class))).thenReturn(resp);

        String payload = TestResourceUtils.readJsonAsString("json/user-register-valid.json");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.token", is("JWT-123")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void registerUser_InvalidPassword_ReturnsBadRequest() throws Exception {
        when(userUseCase.registerUser(any(UserRequest.class)))
                .thenThrow(new IllegalArgumentException("bad password"));

        String payload = TestResourceUtils.readJsonAsString("json/user-register-invalid-password.json");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_DuplicatePhone_ReturnsConflict() throws Exception {
        when(userUseCase.registerUser(any(UserRequest.class)))
                .thenThrow(new PhoneAlreadyExistException("dup"));

        String payload = TestResourceUtils.readJsonAsString("json/user-register-valid.json");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict());
    }
}
