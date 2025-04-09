package com.ibs_demo.invoice_service.service.mapper;

import com.ibs_demo.invoice_service.config.InvoiceServiceConfig;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.model.Role;
import com.ibs_demo.invoice_service.request.UserRequest;
import com.ibs_demo.invoice_service.response.UserResponse;
import com.ibs_demo.invoice_service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    private InvoiceServiceConfig config;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        config = mock(InvoiceServiceConfig.class);
        userMapper = new UserMapper(config);
    }

    @Test
    void testToDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPhoneNumber("1234567890");
        user.setRole(Role.SUPPLIER);

        UserResponse response = userMapper.toDto(user);

        assertEquals(1L, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getName());
        assertEquals("1234567890", response.getPhoneNumber());
        assertEquals(Role.SUPPLIER, response.getRole());
    }

    @Test
    void testToEntity_withValidCountryCode() {
        when(config.getCountryCode()).thenReturn("US");

        UserRequest request = new UserRequest();
        request.setEmail("buyer@example.com");
        request.setName("Buyer Name");
        request.setPhoneNumber("9876543210");
        request.setPassword("Password123@");
        request.setRole(Role.BUYER);

        User user = userMapper.toEntity(request);

        assertEquals("buyer@example.com", user.getEmail());
        assertEquals("Buyer Name", user.getName());
        assertEquals("9876543210", user.getPhoneNumber());
        assertEquals("Password123@", user.getPassword());
        assertEquals(Role.BUYER, user.getRole());
        assertEquals(CountryCode.US, user.getCountryCode());
    }

    @Test
    void testToEntity_withInvalidCountryCode_shouldFallbackToIN() {
        when(config.getCountryCode()).thenReturn("INVALID");

        UserRequest request = new UserRequest();
        request.setEmail("supplier@example.com");
        request.setName("Supplier Name");
        request.setPhoneNumber("1234567890");
        request.setPassword("Supplier@123");
        request.setRole(Role.SUPPLIER);

        User user = userMapper.toEntity(request);

        assertEquals(CountryCode.IN, user.getCountryCode(), "Should fallback to IN on invalid country code");
    }
}
