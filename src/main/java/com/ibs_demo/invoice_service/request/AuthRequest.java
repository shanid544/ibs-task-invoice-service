package com.ibs_demo.invoice_service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;
}