package com.vandana.service;

import com.vandana.entity.User;
import com.vandana.exception.BadRequestException;
import com.vandana.exception.NotFoundException;
import com.vandana.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all workers for an OWNER user
    public List<User> getWorkers(Integer ownerId) {
        return userRepository.findByOwner_UserId(ownerId);
    }

    // Create a new worker owned by OWNER
    public User createWorker(Integer ownerId, String username, String email, String password, String fullName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner user not found"));

        User worker = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .fullName(fullName)
                .role(User.Role.WORKER)
                .owner(owner)
                .status(User.UserStatus.ACTIVE)
                .build();

        return userRepository.save(worker);
    }

    // Delete worker owned by OWNER
    public void deleteWorker(Integer ownerId, Integer workerId) {
        User worker = userRepository.findById(workerId)
                .orElseThrow(() -> new NotFoundException("Worker not found"));

        if (worker.getOwner() == null || !worker.getOwner().getUserId().equals(ownerId)) {
            throw new BadRequestException("You don't have permission to delete this worker");
        }

        userRepository.deleteById(workerId);
    }
}