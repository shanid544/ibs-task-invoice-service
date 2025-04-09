package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.exception.appexceptions.UnauthorizedAccessException;
import com.ibs_demo.invoice_service.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext(); // Clean up after each test
    }

    @Test
    void testGetCurrentUserEmail_whenAuthenticated_returnsEmail() {
        String expectedEmail = "user@example.com";
        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken(expectedEmail, "password");
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String actualEmail = SecurityUtils.getCurrentUserEmail();
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void testGetCurrentUserEmail_whenNotAuthenticated_throwsUnauthorizedAccessException() {
        // No Authentication set in context
        SecurityContextHolder.clearContext();

        assertThrows(UnauthorizedAccessException.class, SecurityUtils::getCurrentUserEmail);
    }

    @Test
    void testGetCurrentUserEmail_whenAuthenticationIsNotAuthenticated_throwsUnauthorizedAccessException() {
        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken("user@example.com", "password");
        authentication.setAuthenticated(false); // explicitly set to not authenticated
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(UnauthorizedAccessException.class, SecurityUtils::getCurrentUserEmail);
    }
}
