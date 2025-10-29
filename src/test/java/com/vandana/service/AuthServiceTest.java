package com.vandana.service;

import com.vandana.dto.request.LoginRequest;
import com.vandana.exception.BadRequestException;
import com.vandana.repository.UserRepository;
import com.vandana.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_InvalidUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        LoginRequest login = new LoginRequest();
        login.setUsername("nonexistent");
        login.setPassword("pass");

        BadRequestException ex = assertThrows(BadRequestException.class, () -> authService.login(login));
        assertEquals("Invalid username or password", ex.getMessage());
    }

    // Add other test cases such as successful login, registration checks etc.
}
