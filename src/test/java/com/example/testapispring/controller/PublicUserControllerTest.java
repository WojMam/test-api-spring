package com.example.testapispring.controller;

import com.example.testapispring.model.User;
import com.example.testapispring.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PublicUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    
    @InjectMocks
    private PublicUserController controller;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        // Given
        User user1 = new User(1L, "user1", null, "user1@example.com", "ROLE_USER");
        User user2 = new User(2L, "user2", null, "user2@example.com", "ROLE_USER");
        List<User> users = Arrays.asList(user1, user2);
        
        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/public/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("user2")));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        // Given
        User user = new User(1L, "user1", null, "user1@example.com", "ROLE_USER");
        when(userService.getUserById(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/public/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(userService.getUserById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/public/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Given
        User inputUser = new User(null, "newuser", "password", "newuser@example.com", null);
        User createdUser = new User(3L, "newuser", null, "newuser@example.com", "ROLE_USER");
        
        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/public/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.username", is("newuser")))
                .andExpect(jsonPath("$.email", is("newuser@example.com")));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        // Given
        User inputUser = new User(null, "updateduser", null, "updated@example.com", null);
        User updatedUser = new User(1L, "updateduser", null, "updated@example.com", "ROLE_USER");
        
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/public/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("updateduser")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        User inputUser = new User(null, "updateduser", null, "updated@example.com", null);
        
        when(userService.updateUser(eq(999L), any(User.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/public/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnNoContent() throws Exception {
        // Given
        when(userService.deleteUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/public/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(userService.deleteUser(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/public/users/999"))
                .andExpect(status().isNotFound());
    }
} 