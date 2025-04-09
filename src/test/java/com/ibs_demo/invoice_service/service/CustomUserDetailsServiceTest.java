package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.model.Role;
import com.ibs_demo.invoice_service.repository.UserRepository;
import com.ibs_demo.invoice_service.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.BUYER);
    }

    @Test
    void testLoadUserByUsername_success() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user@example.com");

        assertEquals("user@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("BUYER")));

        verify(userRepository).findByEmail("user@example.com");
    }

    @Test
    void testLoadUserByUsername_userNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> customUserDetailsService.loadUserByUsername("missing@example.com"));

        verify(userRepository).findByEmail("missing@example.com");
    }
}
