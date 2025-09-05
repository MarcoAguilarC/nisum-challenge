package com.nisum.challenge.application.port.in;

import com.nisum.challenge.application.dto.LoginRequest;
import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;

public interface UserUseCase {

    UserResponse registerUser(UserRequest userRequest);

    UserResponse login(LoginRequest loginRequest);
}