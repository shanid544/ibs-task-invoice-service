package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.config.JwtService;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.exception.appexceptions.DuplicateEmailException;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.model.Role;
import com.ibs_demo.invoice_service.repository.UserRepository;
import com.ibs_demo.invoice_service.request.UserRequest;
import com.ibs_demo.invoice_service.response.UserLoginResponse;
import com.ibs_demo.invoice_service.response.UserRegResponse;
import com.ibs_demo.invoice_service.service.impl.AuthService;
import com.ibs_demo.invoice_service.utils.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserMapper userMapper;
    @Mock private HttpServletRequest httpServletRequest;

    @InjectMocks private AuthService authService;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("Password@123");
        userRequest.setName("Test User");
        userRequest.setPhoneNumber("9876543210");
        userRequest.setRole(Role.SUPPLIER);


        user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(userRequest.getRole());
        user.setCountryCode(CountryCode.IN);
    }

    @Test
    void testRegisterSuccess() {
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UserRegResponse response = authService.register(userRequest, httpServletRequest);

        assertNotNull(response);
        assertEquals("User registered successfully", response.getMessage());
        assertEquals(user.getEmail(), response.getEmail());
        assertTrue(response.getLoginUrl().contains("/api/auth/login"));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterDuplicateEmailThrowsException() {
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateEmailException.class, () -> authService.register(userRequest, httpServletRequest));
    }

    @Test
    void testLoginSuccess() {
        String token = "mockToken";
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(authentication, user.getRole())).thenReturn(token);

        UserLoginResponse response = authService.login(user.getEmail(), user.getPassword());

        assertNotNull(response);
        assertEquals(token, response.getToken());
    }

    @Test
    void testLoginUserNotFoundThrowsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(user.getEmail(), user.getPassword()));
    }
}
