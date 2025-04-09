package com.ibs_demo.invoice_service.utils;

import com.ibs_demo.invoice_service.exception.appexceptions.UnauthorizedAccessException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException();
        }
        return authentication.getName();
    }
}
