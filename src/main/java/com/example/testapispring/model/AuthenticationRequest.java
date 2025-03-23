package com.example.testapispring.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication request containing credentials for login")
public class AuthenticationRequest {
    
    @Schema(description = "Username for authentication", example = "admin", required = true)
    private String username;
    
    @Schema(description = "Password for authentication", example = "admin", required = true)
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
} 