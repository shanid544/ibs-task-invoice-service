package com.ibs_demo.invoice_service.exception.appexceptions;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException() {
        super("Invoice not found");
    }
}