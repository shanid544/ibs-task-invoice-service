package com.ibs_demo.invoice_service.controller;

import com.ibs_demo.invoice_service.request.AuthRequest;
import com.ibs_demo.invoice_service.request.UserRequest;
import com.ibs_demo.invoice_service.response.UserLoginResponse;
import com.ibs_demo.invoice_service.response.UserRegResponse;
import com.ibs_demo.invoice_service.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Handles user registration and login")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Registers a new user with name, email, password, role, and phone number")
    @PostMapping("/register")
    public ResponseEntity<UserRegResponse> register(@RequestBody @Valid UserRequest user,
                                                    HttpServletRequest request) {
        return ResponseEntity.ok(authService.register(user, request));
    }

    @Operation(summary = "Login existing user", description = "Authenticates a user and returns a JWT token")
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }
}
