package com.example.testapispring.controller;

import com.example.testapispring.model.User;
import com.example.testapispring.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SecureUserController.class)
class SecureUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private com.example.testapispring.security.JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @MockBean
    private com.example.testapispring.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void getAllUsers_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/secure/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAllUsers_WithAuthentication_ShouldReturnAllUsers() throws Exception {
        // Given
        User user1 = new User(1L, "user1", null, "user1@example.com", "ROLE_USER");
        User user2 = new User(2L, "user2", null, "user2@example.com", "ROLE_USER");
        List<User> users = Arrays.asList(user1, user2);
        
        given(userService.getAllUsers()).willReturn(users);

        // When & Then
        mockMvc.perform(get("/secure/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("user2")));
    }

    @Test
    @WithMockUser
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        // Given
        User user = new User(1L, "user1", null, "user1@example.com", "ROLE_USER");
        given(userService.getUserById(1L)).willReturn(user);

        // When & Then
        mockMvc.perform(get("/secure/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    @WithMockUser
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        given(userService.getUserById(999L)).willReturn(null);

        // When & Then
        mockMvc.perform(get("/secure/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Given
        User inputUser = new User(null, "newuser", "password", "newuser@example.com", null);
        User createdUser = new User(3L, "newuser", null, "newuser@example.com", "ROLE_USER");
        
        given(userService.createUser(any(User.class))).willReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/secure/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.username", is("newuser")))
                .andExpect(jsonPath("$.email", is("newuser@example.com")));
    }

    @Test
    @WithMockUser
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        // Given
        User inputUser = new User(null, "updateduser", null, "updated@example.com", null);
        User updatedUser = new User(1L, "updateduser", null, "updated@example.com", "ROLE_USER");
        
        given(userService.updateUser(eq(1L), any(User.class))).willReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/secure/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("updateduser")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    @WithMockUser
    void deleteUser_WhenUserExists_ShouldReturnNoContent() throws Exception {
        // Given
        given(userService.deleteUser(1L)).willReturn(true);

        // When & Then
        mockMvc.perform(delete("/secure/users/1"))
                .andExpect(status().isNoContent());
    }
} 