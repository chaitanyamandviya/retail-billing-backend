package com.vandana.service;

import com.vandana.dto.request.LoginRequest;
import com.vandana.dto.request.RegisterRequest;
import com.vandana.dto.response.AuthResponse;
import com.vandana.entity.User;
import com.vandana.exception.BadRequestException;
import com.vandana.repository.UserRepository;
import com.vandana.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse login(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("Invalid username or password");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid username or password");
        }
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new BadRequestException("User not active");
        }

        String accessToken = tokenProvider.generateAccessToken(
                user.getUserId(),
                user.getUsername(),
                user.getRole().toString()
        );

        String refreshToken = tokenProvider.generateRefreshToken(
                user.getUserId(),
                user.getUsername()
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .fullName(user.getFullName())
                .expiresIn(3600L)
                .build();
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists!");
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists!");
        }

        User.Role role;
        try {
            role = User.Role.valueOf(req.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + req.getRole());
        }

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .role(role)
                .status(User.UserStatus.ACTIVE)
                .build();

        User saved = userRepository.save(user);

        return AuthResponse.builder()
                .userId(saved.getUserId())
                .username(saved.getUsername())
                .role(saved.getRole().toString())
                .fullName(saved.getFullName())
                .accessToken(null)    // NULL on registration until login
                .refreshToken(null)
                .expiresIn(null)
                .build();
    }
}