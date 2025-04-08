package com.ibs_demo.invoice_service.exception.appexceptions;

public class InvoiceAccessDeniedException extends RuntimeException {
    public InvoiceAccessDeniedException(String message) {
        super(message);
    }
}
