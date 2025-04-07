package com.ibs_demo.invoice_service.service.impl;

import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.service.TaxService;
import org.springframework.stereotype.Component;

@Component
public class IndiaTaxService implements TaxService {
    @Override
    public CountryCode  getCountryCode() {
        return CountryCode.IN;
    }

    @Override
    public double calculateTax(double amount) {
        return amount * 0.18;
    }
}