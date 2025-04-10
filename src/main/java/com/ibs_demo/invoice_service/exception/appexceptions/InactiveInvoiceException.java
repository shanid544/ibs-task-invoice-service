package com.ibs_demo.invoice_service.exception.appexceptions;

public class InactiveInvoiceException extends RuntimeException {
    public InactiveInvoiceException(Long invoiceId) {
        super("Invoice with billing ID '" + invoiceId + "' is inactive.");
    }
}
