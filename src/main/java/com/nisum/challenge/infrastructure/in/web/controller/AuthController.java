package com.nisum.challenge.infrastructure.in.web.controller;

import com.nisum.challenge.application.dto.LoginRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.application.port.in.UserUseCase;
import com.nisum.challenge.application.util.MessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserUseCase userUseCase;
    private final MessageService messageService;

    public AuthController(UserUseCase userUseCase, MessageService messageService) {
        this.userUseCase = userUseCase;
        this.messageService = messageService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info(messageService.getMessage("log.info.controller.login.request"));
        UserResponse response = userUseCase.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}