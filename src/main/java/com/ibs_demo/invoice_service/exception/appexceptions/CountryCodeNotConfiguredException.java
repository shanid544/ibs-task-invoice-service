package com.ibs_demo.invoice_service.exception.appexceptions;

public class CountryCodeNotConfiguredException extends RuntimeException {
    public CountryCodeNotConfiguredException() {
        super("Country code not configured. Please set 'invoice-service.country-code' in application properties.");
    }
}