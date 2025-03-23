package com.example.testapispring.service;

import com.example.testapispring.exception.DuplicateResourceException;
import com.example.testapispring.exception.ResourceNotFoundException;
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
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        return sanitizeUser(user);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        return sanitizeUser(user);
    }

    public User createUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }
        
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
            throw new ResourceNotFoundException("User", "id", id);
        }

        // Check if the new username conflicts with another user
        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(existingUser.getUsername())) {
            User existingByUsername = userRepository.findByUsername(userDetails.getUsername());
            if (existingByUsername != null && !existingByUsername.getId().equals(id)) {
                throw new DuplicateResourceException("User", "username", userDetails.getUsername());
            }
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

    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
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