package com.nisum.challenge.infrastructure.out.security;

import com.nisum.challenge.application.util.MessageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderAdapterTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private JwtTokenProviderAdapter jwtTokenProviderAdapter;

    private final String secret = "a-very-long-and-secure-secret-key-for-testing-purposes-only-1234567890";
    private final long expiration = 3600000;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProviderAdapter, "secretString", secret);
        ReflectionTestUtils.setField(jwtTokenProviderAdapter, "expirationInMs", expiration);
        jwtTokenProviderAdapter.init();
    }

    @Test
    void generateToken_createsValidToken() {
        String email = "test@example.com";

        String token = jwtTokenProviderAdapter.generateToken(email);

        assertNotNull(token);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        assertEquals(email, claims.getSubject());
    }
}
