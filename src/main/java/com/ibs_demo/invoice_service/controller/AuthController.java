package com.ibs_demo.invoice_service.controller;

import com.ibs_demo.invoice_service.request.AuthRequest;
import com.ibs_demo.invoice_service.request.UserRequest;
import com.ibs_demo.invoice_service.response.UserLoginResponse;
import com.ibs_demo.invoice_service.response.UserRegResponse;
import com.ibs_demo.invoice_service.service.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserRegResponse> register(@RequestBody @Valid UserRequest user,
                                                    HttpServletRequest request) {
        return ResponseEntity.ok(authService.register(user, request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }
}
