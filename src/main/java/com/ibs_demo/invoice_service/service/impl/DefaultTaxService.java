package com.ibs_demo.invoice_service.service.impl;

import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.service.TaxService;
import org.springframework.stereotype.Component;

@Component
public class DefaultTaxService implements TaxService {

    @Override
    public CountryCode getCountryCode() {
        return null;
    }

    @Override
    public double calculateTax(double amount) {
        return 0.0;
    }
}
