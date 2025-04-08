package com.ibs_demo.invoice_service.exception.appexceptions;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message) {
        super(message);
    }
}
