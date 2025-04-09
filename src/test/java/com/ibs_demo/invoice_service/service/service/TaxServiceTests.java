package com.ibs_demo.invoice_service.service.service;

import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.service.impl.DefaultTaxService;
import com.ibs_demo.invoice_service.service.impl.IndiaTaxService;
import com.ibs_demo.invoice_service.service.impl.UKTaxService;
import com.ibs_demo.invoice_service.service.impl.USTaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaxServiceTests {

    private IndiaTaxService indiaTaxService;
    private UKTaxService ukTaxService;
    private USTaxService usTaxService;
    private DefaultTaxService defaultTaxService;

    @BeforeEach
    void setup() {
        indiaTaxService = new IndiaTaxService();
        ukTaxService = new UKTaxService();
        usTaxService = new USTaxService();
        defaultTaxService = new DefaultTaxService();
    }

    @Test
    void testIndiaTaxService() {
        double tax = indiaTaxService.calculateTax(1000.0);
        assertEquals(180.0, tax, 0.001);
        assertEquals(CountryCode.IN, indiaTaxService.getCountryCode());
    }

    @Test
    void testUKTaxService() {
        double tax = ukTaxService.calculateTax(1000.0);
        assertEquals(200.0, tax, 0.001);
        assertEquals(CountryCode.UK, ukTaxService.getCountryCode());
    }

    @Test
    void testUSTaxService() {
        double tax = usTaxService.calculateTax(1000.0);
        assertEquals(70.0, tax, 0.001);
        assertEquals(CountryCode.US, usTaxService.getCountryCode());
    }

    @Test
    void testDefaultTaxService() {
        double tax = defaultTaxService.calculateTax(1000.0);
        assertEquals(0.0, tax, 0.001);
        assertNull(defaultTaxService.getCountryCode());
    }
}
