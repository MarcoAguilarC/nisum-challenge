package com.nisum.challenge.infrastructure.out.security;

import com.nisum.challenge.application.port.out.JwtTokenProviderPort;
import com.nisum.challenge.application.util.MessageService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProviderAdapter implements JwtTokenProviderPort {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProviderAdapter.class);

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration-ms}")
    private long expirationInMs;

    private SecretKey secretKey;
    private final MessageService messageService;

    public JwtTokenProviderAdapter(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationInMs);

        String token = Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();

        logger.info(messageService.getMessage("log.info.jwt.generated", new Object[]{email}));
        return token;
    }
}