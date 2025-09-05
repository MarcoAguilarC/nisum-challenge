package com.nisum.challenge.application.service;

import com.nisum.challenge.application.dto.LoginRequest;
import com.nisum.challenge.application.dto.PhoneDTO;
import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.application.mapper.UserMapper;
import com.nisum.challenge.application.port.out.JwtTokenProviderPort;
import com.nisum.challenge.application.port.out.PhoneRepositoryPort;
import com.nisum.challenge.application.port.out.UserRepositoryPort;
import com.nisum.challenge.application.util.MessageService;
import com.nisum.challenge.domain.exception.PhoneAlreadyExistException;
import com.nisum.challenge.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;
    @Mock private JwtTokenProviderPort jwtTokenProvider;
    @Mock private PhoneRepositoryPort phoneRepositoryPort;
    @Mock private MessageService messageService;

    @InjectMocks private UserServiceImpl userService;

    private UserRequest req;
    private User mappedUser;

    @BeforeEach
    void setUp() {
        req = new UserRequest();
        req.setName("test user");
        req.setEmail("test@example.com");
        req.setPassword("Password123!");

        mappedUser = new User();
        mappedUser.setEmail("test@example.com");
        mappedUser.setPassword("encoded");

        lenient().when(messageService.getMessage(anyString(), any())).thenReturn("some message");
    }

    @Test
    void registerUser_generatesJwtBeforeSave() {
        when(userRepositoryPort.findByEmail("test@example.com")).thenReturn(Optional.empty());
        req.setPhones(List.of(new PhoneDTO("12345678", "9", "56")));
        when(phoneRepositoryPort.existsByCountryCodeAndCityCodeAndNumber(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(jwtTokenProvider.generateToken("test@example.com")).thenReturn("JWT-123");
        when(userRepositoryPort.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toUser(any(UserRequest.class))).thenReturn(mappedUser);
        when(userMapper.toUserResponse(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return UserResponse.builder()
                    .lastLogin(u.getLastLogin())
                    .token(u.getToken())
                    .isActive(u.isActive())
                    .build();
        });

        UserResponse res = userService.registerUser(req);

        assertThat(res.getToken()).isEqualTo("JWT-123");

        ArgumentCaptor<User> userCap = ArgumentCaptor.forClass(User.class);
        InOrder inOrder = inOrder(jwtTokenProvider, userRepositoryPort);
        inOrder.verify(jwtTokenProvider).generateToken("test@example.com");
        inOrder.verify(userRepositoryPort).save(userCap.capture());

        User saved = userCap.getValue();
        assertThat(saved.getToken()).isEqualTo("JWT-123");
    }

    @Test
    void registerUser_throws_whenPhoneExists() {
        when(userRepositoryPort.findByEmail("test@example.com")).thenReturn(Optional.empty());
        req.setPhones(List.of(new PhoneDTO("12345678", "9", "56")));
        when(phoneRepositoryPort.existsByCountryCodeAndCityCodeAndNumber(anyString(), anyString(), anyString()))
                .thenReturn(true);

        assertThrows(PhoneAlreadyExistException.class, () -> userService.registerUser(req));

        verify(jwtTokenProvider, never()).generateToken(anyString());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void login_setsTokenAndLastLogin_andPersists() {
        LoginRequest login = new LoginRequest();
        login.setEmail("test@example.com");
        login.setPassword("Password123!");

        User existing = new User();
        existing.setEmail("test@example.com");
        existing.setPassword("encoded");

        when(userRepositoryPort.findByEmail("test@example.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenProvider.generateToken("test@example.com")).thenReturn("JWT-999");
        when(userRepositoryPort.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toUserResponse(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return UserResponse.builder()
                    .lastLogin(u.getLastLogin())
                    .token(u.getToken())
                    .isActive(u.isActive())
                    .build();
        });

        UserResponse res = userService.login(login);

        assertThat(res.getToken()).isEqualTo("JWT-999");
        verify(userRepositoryPort).save(argThat(u ->
                "JWT-999".equals(u.getToken()) && u.getLastLogin() != null
        ));
    }
}
