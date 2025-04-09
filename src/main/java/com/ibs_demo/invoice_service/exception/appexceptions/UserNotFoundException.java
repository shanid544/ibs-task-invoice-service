package com.ibs_demo.invoice_service.exception.appexceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}