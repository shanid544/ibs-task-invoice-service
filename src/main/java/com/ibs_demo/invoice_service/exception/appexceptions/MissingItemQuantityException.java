package com.ibs_demo.invoice_service.exception.appexceptions;

public class MissingItemQuantityException extends RuntimeException {
    public MissingItemQuantityException(Long itemId) {
        super("Quantity not found for item ID: " + itemId);
    }
}
