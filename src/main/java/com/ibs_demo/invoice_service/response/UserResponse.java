package com.ibs_demo.invoice_service.response;

import com.ibs_demo.invoice_service.model.CountryCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.ibs_demo.invoice_service.model.Role; // Import Role Enum
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private Role role;
    private CountryCode countryCode;
    private String status;
}
