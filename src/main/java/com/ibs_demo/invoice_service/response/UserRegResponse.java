package com.ibs_demo.invoice_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegResponse {
    private String message;
    private String email;
    private String loginUrl;
}
