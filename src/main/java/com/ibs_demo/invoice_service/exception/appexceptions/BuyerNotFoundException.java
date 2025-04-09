package com.ibs_demo.invoice_service.exception.appexceptions;

public class BuyerNotFoundException extends RuntimeException {
    public BuyerNotFoundException(Long buyerId) {
        super("Buyer not found with ID: " + buyerId);
    }
    public BuyerNotFoundException(String email) {
        super("Buyer not found with email: " + email);
    }
}
