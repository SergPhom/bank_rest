package com.example.bankcards.service.api;

import com.example.bankcards.dto.auth.JwtAuthenticationResponse;
import com.example.bankcards.dto.auth.SignInRequest;
import com.example.bankcards.dto.auth.SignUpRequest;

public interface AuthService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}
