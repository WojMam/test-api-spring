package com.example.testapispring.controller;

import com.example.testapispring.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecuredControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void publicMessage_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/public/message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a public endpoint"));
    }

    @Test
    void secureMessage_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/secure/message"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void secureMessage_WithAuthentication_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/secure/message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a secured endpoint with JWT"));
    }

    @Test
    void basicAuthMessage_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/basic-auth/message"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void basicAuthMessage_WithAuthentication_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/basic-auth/message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a secured endpoint with Basic Auth"));
    }
} 