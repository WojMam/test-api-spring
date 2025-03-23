package com.example.testapispring.model;

import com.example.testapispring.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

@Schema(description = "User entity representing a user of the system")
public class User {
    
    @Schema(description = "Unique identifier for the user", example = "1")
    private Long id;
    
    @Schema(description = "Username for authentication", example = "john_doe", required = true)
    @NotBlank(message = "Nazwa użytkownika jest wymagana", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 3, max = 50, message = "Nazwa użytkownika musi mieć od 3 do 50 znaków", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Nazwa użytkownika może zawierać tylko litery, cyfry, kropki, myślniki i podkreślenia", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String username;
    
    @Schema(description = "Password for authentication (never returned in responses)", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Hasło jest wymagane", groups = {ValidationGroups.Create.class})
    @Size(min = 6, max = 100, message = "Hasło musi mieć od 6 do 100 znaków", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String password;
    
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Email jest wymagany", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Email(message = "Podaj prawidłowy adres email", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String email;
    
    @Schema(description = "User's role in the system", example = "ROLE_USER", allowableValues = {"ROLE_ADMIN", "ROLE_USER"}, required = true)
    @NotBlank(message = "Rola jest wymagana", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$", message = "Dozwolone role to ROLE_ADMIN lub ROLE_USER", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String role;

    public User() {
    }

    public User(Long id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
} 