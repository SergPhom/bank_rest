package com.example.bankcards.controller;

import com.example.bankcards.BaseTester;
import com.example.bankcards.dto.auth.SignInRequest;
import com.example.bankcards.dto.auth.SignUpRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseTester {

    @Test
    void signUp() throws Exception {
        SignUpRequest request = new SignUpRequest()
                .setUsername("someName")
                .setEmail("user@example.com")
                .setPassword("password123");

        var response = mockMvc.perform(post("/bank/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertTrue(response.matches(".*\"token\":\"([^\"]+)\".*"));
    }

    @Test
    void signIn() throws Exception {
        SignInRequest request = new SignInRequest()
                .setUsername("user@example.com")
                .setPassword("password");
        var user = User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .roles(Set.of(Role.ROLE_USER))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken(new Object(), new Object()));

        var response = mockMvc.perform(post("/bank/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertTrue(response.matches(".*\"token\":\"([^\"]+)\".*"));
    }
}