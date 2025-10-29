package com.vandana.service;

import com.vandana.entity.User;
import com.vandana.exception.BadRequestException;
import com.vandana.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWorker_Success() {
        User owner = User.builder().userId(1).username("owner").build();

        when(userRepository.findByUsername("new_worker")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("worker@example.com")).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(passwordEncoder.encode("password123")).thenReturn("hashed_pass");

        User savedUser = User.builder().userId(2).username("new_worker").build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createWorker(1, "new_worker", "worker@example.com", "password123", "Worker Name");
        assertNotNull(result);
        assertEquals("new_worker", result.getUsername());
    }

    @Test
    void testCreateWorker_UsernameExists() {
        when(userRepository.findByUsername("existing"))
                .thenReturn(Optional.of(new User()));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            userService.createWorker(1, "existing", "newemail@example.com", "pass", "Name");
        });
        assertEquals("Username already exists", ex.getMessage());
    }
}
