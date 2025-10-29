package com.vandana.controller;

import com.vandana.dto.request.RegisterRequest;
import com.vandana.dto.response.AuthResponse;
import com.vandana.entity.User;
import com.vandana.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class UserController {

    @Autowired
    private UserService userService;

    // Get all workers owned by logged-in OWNER
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/workers")
    public ResponseEntity<List<AuthResponse>> getWorkers(Authentication authentication) {
        Integer ownerId = (Integer) authentication.getPrincipal();
        List<User> workers = userService.getWorkers(ownerId);

        List<AuthResponse> responses = workers.stream()
                .map(user -> AuthResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .role(user.getRole().toString())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Create a new worker user as OWNER
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/workers")
    public ResponseEntity<AuthResponse> createWorker(Authentication authentication,
                                                     @Valid @RequestBody RegisterRequest request) {
        Integer ownerId = (Integer) authentication.getPrincipal();

        User worker = userService.createWorker(ownerId,
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFullName());

        AuthResponse response = AuthResponse.builder()
                .userId(worker.getUserId())
                .username(worker.getUsername())
                .fullName(worker.getFullName())
                .role(worker.getRole().toString())
                .build();

        return ResponseEntity.ok(response);
    }

    // Delete a worker owned by OWNER
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/workers/{workerId}")
    public ResponseEntity<Void> deleteWorker(Authentication authentication,
                                             @PathVariable Integer workerId) {
        Integer ownerId = (Integer) authentication.getPrincipal();
        userService.deleteWorker(ownerId, workerId);
        return ResponseEntity.ok().build();
    }
}