package com.nisum.challenge.application.mapper;

import com.nisum.challenge.application.dto.PhoneDTO;
import com.nisum.challenge.application.dto.UserRequest;
import com.nisum.challenge.application.dto.UserResponse;
import com.nisum.challenge.domain.model.Phone;
import com.nisum.challenge.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PhoneMapper phoneMapper;

    @Test
    void userMapper_toUserResponse_mapsCorrectly() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("test user");
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken("test-token");
        user.setActive(true);
        user.setPhones(Collections.singletonList(new Phone(1L, "123", "456", "789")));

        UserResponse response = userMapper.toUserResponse(user);

        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getCreated(), response.getCreated());
        assertEquals(user.getModified(), response.getModified());
        assertEquals(user.getLastLogin(), response.getLastLogin());
        assertEquals(user.getToken(), response.getToken());
        assertEquals(user.isActive(), response.isActive());
    }

    @Test
    void userMapper_toUser_mapsCorrectly() {
        UserRequest request = new UserRequest();
        request.setName("test user");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setPhones(Collections.singletonList(new PhoneDTO("12345678", "9", "56")));

        User user = userMapper.toUser(request);

        assertNotNull(user);
        assertEquals(request.getName(), user.getName());
        assertEquals(request.getEmail(), user.getEmail());
        assertEquals(request.getPassword(), user.getPassword());
        assertNull(user.getId());
        assertNull(user.getCreated());
        assertNull(user.getModified());
        assertNull(user.getLastLogin());
        assertNull(user.getToken());
        assertFalse(user.isActive());
        assertNotNull(user.getPhones());
        assertEquals(1, user.getPhones().size());
    }

    @Test
    void phoneMapper_toPhone_mapsCorrectly() {
        PhoneDTO dto = new PhoneDTO("12345678", "9", "56");
        Phone phone = phoneMapper.toPhone(dto);

        assertNotNull(phone);
        assertEquals(dto.getNumber(), phone.getNumber());
        assertEquals(dto.getCityCode(), phone.getCityCode());
        assertEquals(dto.getCountryCode(), phone.getCountryCode());
        assertNull(phone.getId());
    }
}
