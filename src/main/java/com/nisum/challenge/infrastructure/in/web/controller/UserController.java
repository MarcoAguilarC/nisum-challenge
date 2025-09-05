package com.nisum.challenge.infrastructure.in.web.controller;

import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.application.port.in.UserUseCase;
import com.nisum.challenge.application.util.MessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserUseCase userUseCase;
    private final MessageService messageService;

    public UserController(UserUseCase userUseCase, MessageService messageService) {
        this.userUseCase = userUseCase;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        logger.info(messageService.getMessage("log.info.controller.register.request"));
        UserResponse response = userUseCase.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}