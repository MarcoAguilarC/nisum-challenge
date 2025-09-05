package com.nisum.challenge.application.port.out;

import com.nisum.challenge.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);
}