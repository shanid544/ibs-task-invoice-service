package com.ibs_demo.invoice_service.exception.appexceptions;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(String email) {
        super("Supplier not found with email: " + email);
    }
}
