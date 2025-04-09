package com.ibs_demo.invoice_service.service.config;

import com.ibs_demo.invoice_service.config.JwtAuthenticationFilter;
import com.ibs_demo.invoice_service.config.SecurityConfig;
import com.ibs_demo.invoice_service.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityConfigTest {

    private JwtAuthenticationFilter jwtAuthFilter;
    private CustomUserDetailsService userDetailsService;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = mock(JwtAuthenticationFilter.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        securityConfig = new SecurityConfig(jwtAuthFilter, userDetailsService);
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testAuthenticationProviderBean() {
        AuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider);
        assertTrue(provider instanceof DaoAuthenticationProvider);
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        when(config.getAuthenticationManager()).thenReturn(manager);

        AuthenticationManager result = securityConfig.authenticationManager(config);
        assertEquals(manager, result);
    }
}
