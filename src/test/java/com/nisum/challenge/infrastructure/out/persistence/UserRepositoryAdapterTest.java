package com.nisum.challenge.infrastructure.out.persistence;

import com.nisum.challenge.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
    void save_delegatesToJpaRepository() {
        User user = new User();
        when(userJpaRepository.save(user)).thenReturn(user);

        User savedUser = userRepositoryAdapter.save(user);

        assertEquals(user, savedUser);
        verify(userJpaRepository, times(1)).save(user);
    }

    @Test
    void findByEmail_delegatesToJpaRepository() {
        String email = "test@example.com";
        User user = new User();
        when(userJpaRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepositoryAdapter.findByEmail(email);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userJpaRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_returnsEmptyOptionalWhenNotFound() {
        String email = "notfound@example.com";
        when(userJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepositoryAdapter.findByEmail(email);

        assertTrue(foundUser.isEmpty());
        verify(userJpaRepository, times(1)).findByEmail(email);
    }
}
