package com.ibs_demo.invoice_service.exception.appexceptions;

public class InvalidCountryCodeException extends RuntimeException {
    public InvalidCountryCodeException(String countryCode, Throwable cause) {
        super("Invalid country code configured: " + countryCode, cause);
    }


}
