package com.ibs_demo.invoice_service.utils;

import com.ibs_demo.invoice_service.config.InvoiceServiceConfig;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.request.UserRequest;
import com.ibs_demo.invoice_service.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final InvoiceServiceConfig config;

    public UserResponse toDto(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        return dto;
    }

    public User toEntity(UserRequest dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        try {
            user.setCountryCode(CountryCode.valueOf(config.getCountryCode()));
        } catch (IllegalArgumentException e) {
            user.setCountryCode(CountryCode.IN); // default fallback
        }
        return user;
    }
}
