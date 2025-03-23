package com.example.testapispring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SecuredControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private SecuredController securedController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(securedController).build();
    }
    
    @Test
    void publicMessage_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/public/message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a public endpoint"));
    }

    @Test
    void secureMessage_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/secure/message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a secured endpoint with JWT"));
    }

    @Test
    void basicAuthMessage_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/basic-auth/message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a secured endpoint with Basic Auth"));
    }
} 