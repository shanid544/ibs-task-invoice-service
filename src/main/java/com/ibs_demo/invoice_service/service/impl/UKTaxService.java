package com.ibs_demo.invoice_service.service.impl;

import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.service.TaxService;
import org.springframework.stereotype.Component;

@Component
public class UKTaxService implements TaxService {
    @Override
    public CountryCode  getCountryCode() {
        return CountryCode.UK;
    }

    @Override
    public double calculateTax(double amount) {
        return amount * 0.20;
    }
}