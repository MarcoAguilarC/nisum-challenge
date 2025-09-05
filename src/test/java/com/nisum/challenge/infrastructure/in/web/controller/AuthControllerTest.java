package com.nisum.challenge.infrastructure.in.web.controller;

import com.nisum.challenge.application.dto.LoginRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.application.port.in.UserUseCase;
import com.nisum.challenge.application.util.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_ReturnsOk() {
        LoginRequest loginRequest = new LoginRequest();
        UserResponse userResponse = UserResponse.builder().build();

        when(userUseCase.login(any(LoginRequest.class))).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userUseCase, times(1)).login(loginRequest);
        verify(messageService, times(1)).getMessage("log.info.controller.login.request");
    }
}
