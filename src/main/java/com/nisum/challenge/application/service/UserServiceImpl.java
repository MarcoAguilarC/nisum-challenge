package com.nisum.challenge.application.service;

import com.nisum.challenge.application.dto.LoginRequest;
import com.nisum.challenge.application.dto.PhoneDTO;
import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.application.mapper.UserMapper;
import com.nisum.challenge.application.port.in.UserUseCase;
import com.nisum.challenge.application.port.out.JwtTokenProviderPort;
import com.nisum.challenge.application.port.out.PhoneRepositoryPort;
import com.nisum.challenge.application.port.out.UserRepositoryPort;
import com.nisum.challenge.application.util.MessageService;
import com.nisum.challenge.domain.exception.PhoneAlreadyExistException;
import com.nisum.challenge.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenProviderPort jwtTokenProvider;
    private final PhoneRepositoryPort phoneRepositoryPort;
    private final MessageService messageService;

    public UserServiceImpl(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder, UserMapper userMapper, JwtTokenProviderPort jwtTokenProvider, PhoneRepositoryPort phoneRepositoryPort, MessageService messageService) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.phoneRepositoryPort = phoneRepositoryPort;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public UserResponse registerUser(UserRequest userRequest) {
        logger.info(messageService.getMessage("log.info.register.start", new Object[]{userRequest.getEmail()}));

        userRepositoryPort.findByEmail(userRequest.getEmail()).ifPresent(user -> {
            logger.warn(messageService.getMessage("log.warn.email.exists", new Object[]{userRequest.getEmail()}));
            throw new IllegalArgumentException("error.email.exists");
        });

        if (userRequest.getPhones() != null) {
            for (PhoneDTO phoneDto : userRequest.getPhones()) {
                if (phoneRepositoryPort.existsByCountryCodeAndCityCodeAndNumber(
                        phoneDto.getCountryCode(), phoneDto.getCityCode(), phoneDto.getNumber())) {
                    logger.warn(messageService.getMessage("log.warn.phone.exists.service", new Object[]{phoneDto.getNumber()}));
                    throw new PhoneAlreadyExistException("error.phone.exists", phoneDto.getNumber());
                }
            }
        }

        User newUser = userMapper.toUser(userRequest);
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        String token = jwtTokenProvider.generateToken(newUser.getEmail());
        newUser.setToken(token);

        User savedUser = userRepositoryPort.save(newUser);
        logger.info(messageService.getMessage("log.info.register.success", new Object[]{savedUser.getEmail()}));

        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse login(LoginRequest loginRequest) {
        logger.info(messageService.getMessage("log.info.login.start", new Object[]{loginRequest.getEmail()}));

        User user = userRepositoryPort.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("error.credentials.invalid"));


        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("error.credentials.invalid");
        }

        String newToken = jwtTokenProvider.generateToken(user.getEmail());
        user.setToken(newToken);
        user.setLastLogin(LocalDateTime.now());

        User updatedUser = userRepositoryPort.save(user);
        logger.info(messageService.getMessage("log.info.login.success", new Object[]{updatedUser.getEmail()}));

        return userMapper.toUserResponse(updatedUser);
    }
}
