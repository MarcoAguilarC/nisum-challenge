package com.nisum.challenge.application.port.out;

public interface JwtTokenProviderPort {
    String generateToken(String email);
}
