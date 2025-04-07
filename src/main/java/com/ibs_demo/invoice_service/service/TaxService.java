package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.model.CountryCode;

public interface TaxService {
    CountryCode getCountryCode();
    double calculateTax(double amount);
}
