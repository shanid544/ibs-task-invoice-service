package com.ibs_demo.invoice_service.exception.appexceptions;

public class InvalidJwtTokenException extends RuntimeException {

    public InvalidJwtTokenException() {
        super("Invalid JWT token");
    }
}