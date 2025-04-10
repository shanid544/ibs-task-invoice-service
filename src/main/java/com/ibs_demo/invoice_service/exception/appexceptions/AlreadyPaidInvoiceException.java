package com.ibs_demo.invoice_service.exception.appexceptions;

public class AlreadyPaidInvoiceException extends RuntimeException {
    public AlreadyPaidInvoiceException(Long invoiceId) {
        super("Invoice with billing ID '" + invoiceId + "' is already paid.");
    }
}
