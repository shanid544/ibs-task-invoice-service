package com.ibs_demo.invoice_service.request;

import com.ibs_demo.invoice_service.annotations.EnumValidator;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.ibs_demo.invoice_service.model.Role; // Import Role Enum

@Data
public class UserRequest {

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "^[^<>]*$",
            message = "Name must not contain HTML or SQL tags"
    )
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;

    @NotNull(message = "Phone number is required")
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be exactly 10 digits"
    )
    private String phoneNumber;

    @EnumValidator(enumClass = Role.class, message = "Role must be either SUPPLIER or BUYER")
    @NotNull(message = "Role is required")
    private Role role;
}
