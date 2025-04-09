package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.config.JwtService;
import com.ibs_demo.invoice_service.model.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secret = "my-very-secret-key-for-testing-my-very-secret-key-for-testing";
    private final String expiration = "3600000"; // 1 hour in ms

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        // Set private fields using reflection
        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, secret);

        Field expirationField = JwtService.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, expiration);

        jwtService.init();
    }

    @Test
    void testGenerateAndValidateToken() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser@example.com");

        String token = jwtService.generateToken(auth, Role.BUYER);

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
        assertEquals("testuser@example.com", jwtService.extractUsername(token));
        assertEquals("BUYER", jwtService.extractRole(token));
    }

    @Test
    void testInvalidTokenValidationFails() {
        String invalidToken = "this.is.not.a.valid.token";

        assertFalse(jwtService.validateToken(invalidToken));
    }

    @Test
    void testExtractAllClaims() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("claimsuser");

        String token = jwtService.generateToken(auth, Role.SUPPLIER);
        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("claimsuser", claims.getSubject());
        assertEquals("SUPPLIER", claims.get("roles"));
    }

    @Test
    void testGetExpirationTime() {
        long expected = Long.parseLong(expiration);
        assertEquals(expected, jwtService.getExpirationTime());
    }
}
