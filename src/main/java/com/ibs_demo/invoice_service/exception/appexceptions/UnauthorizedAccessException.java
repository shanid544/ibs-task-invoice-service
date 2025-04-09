package com.ibs_demo.invoice_service.exception.appexceptions;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("Unauthorized access: User is not authenticated.");
    }
}
