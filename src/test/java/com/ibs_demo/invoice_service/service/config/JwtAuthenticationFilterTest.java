package com.ibs_demo.invoice_service.service.config;

import com.ibs_demo.invoice_service.config.JwtAuthenticationFilter;
import com.ibs_demo.invoice_service.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtService jwtService;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setup() {
        jwtService = mock(JwtService.class);
        filter = new JwtAuthenticationFilter(jwtService);

        // Clear SecurityContext before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthenticationWhenValidToken() throws Exception {
        String token = "valid.jwt.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtService.validateToken(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn("testuser@example.com");
        when(jwtService.extractRole(token)).thenReturn("BUYER");

        filter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testuser@example.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals("ROLE_BUYER", SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority());

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenNoHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(); // No Authorization header
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldReturnUnauthorizedWhenInvalidToken() throws Exception {
        String token = "invalid.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtService.validateToken(token)).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Invalid token"));
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void shouldReturnUnauthorizedWhenExceptionThrownDuringProcessing() throws Exception {
        String token = "bad.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtService.validateToken(token)).thenThrow(new RuntimeException("JWT parsing failed"));

        filter.doFilterInternal(request, response, chain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Invalid token"));
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(chain, never()).doFilter(any(), any());
    }
}
