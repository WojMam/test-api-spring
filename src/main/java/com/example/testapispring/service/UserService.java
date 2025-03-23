package com.example.testapispring.service;

import com.example.testapispring.model.User;
import com.example.testapispring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::sanitizeUser)
                .toList();
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id);
        return user != null ? sanitizeUser(user) : null;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? sanitizeUser(user) : null;
    }

    public User createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        
        return sanitizeUser(userRepository.save(user));
    }

    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return null;
        }

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        
        // Only update password if it's provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        // Only update role if it's provided
        if (userDetails.getRole() != null && !userDetails.getRole().isEmpty()) {
            existingUser.setRole(userDetails.getRole());
        }

        return sanitizeUser(userRepository.save(existingUser));
    }

    public boolean deleteUser(Long id) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
    
    // Helper method to remove sensitive information like passwords from returned users
    private User sanitizeUser(User user) {
        User sanitizedUser = new User();
        sanitizedUser.setId(user.getId());
        sanitizedUser.setUsername(user.getUsername());
        sanitizedUser.setEmail(user.getEmail());
        sanitizedUser.setRole(user.getRole());
        return sanitizedUser;
    }
} 