package com.example.authservice.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authservice.config.JWTUtil;
import com.example.authservice.dto.*;
import com.example.authservice.model.AuthUser;
import com.example.authservice.repository.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterResponse registerUser(RegisterRequest registerRequest) {

        if (authUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        AuthUser authUser = new AuthUser();
        authUser.setEmail(registerRequest.getEmail());
        authUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        authUser.setRole("USER");
        authUserRepository.save(authUser);
        return new RegisterResponse(registerRequest.getEmail() + " registered successfully");
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        AuthUser authUser = authUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), authUser.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(authUser.getEmail());

        return new LoginResponse(loginRequest.getEmail(), token);
    }

    // Called by Task Service via internal endpoint to verify a token
    public boolean validateToken(String token) {
        return jwtUtil.validateJwtToken(token);
    }

    // Extract email from token (Task Service needs this)
    public String getEmailFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
}
