package com.ibs_demo.invoice_service.service.impl;

import com.ibs_demo.invoice_service.config.JwtService;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.exception.customExceptions.DuplicateEmailException;
import com.ibs_demo.invoice_service.repository.UserRepository;
import com.ibs_demo.invoice_service.request.UserRequest;
import com.ibs_demo.invoice_service.response.UserLoginResponse;
import com.ibs_demo.invoice_service.response.UserRegResponse;
import com.ibs_demo.invoice_service.utils.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public UserRegResponse register(UserRequest userRequest, HttpServletRequest request) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = userMapper.toEntity(userRequest);
        try {
        userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException("Email already registered, try to use other mail");
        }
        return buildUserRegResponse(user,request);
    }

    private UserRegResponse buildUserRegResponse(User user, HttpServletRequest request) {
        String loginUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath() + "/api/auth/login";

        return new UserRegResponse(
                "User registered successfully",
                user.getEmail(),
                loginUrl
        );

    }

    public UserLoginResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserLoginResponse(jwtService.generateToken(authentication, user.getRole()));
    }
}
