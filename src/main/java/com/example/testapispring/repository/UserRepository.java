package com.example.testapispring.repository;

import com.example.testapispring.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);
    private final PasswordEncoder passwordEncoder;

    public UserRepository(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers() {
        // Create admin user
        User admin = new User();
        admin.setId(idCounter.incrementAndGet());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@example.com");
        admin.setRole("ROLE_ADMIN");
        users.put(admin.getId(), admin);

        // Create regular user
        User user = new User();
        user.setId(idCounter.incrementAndGet());
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("user@example.com");
        user.setRole("ROLE_USER");
        users.put(user.getId(), user);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.incrementAndGet());
        }
        users.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        users.remove(id);
    }
} 