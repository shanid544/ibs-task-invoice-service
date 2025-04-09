package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.service.impl.DefaultTaxService;
import com.ibs_demo.invoice_service.utils.TaxServiceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaxServiceProviderTest {

    private TaxService indiaTaxService;
    private TaxService usaTaxService;
    private DefaultTaxService defaultTaxService;

    private TaxServiceProvider taxServiceProvider;

    @BeforeEach
    void setUp() {
        indiaTaxService = mock(TaxService.class);
        when(indiaTaxService.getCountryCode()).thenReturn(CountryCode.IN);

        usaTaxService = mock(TaxService.class);
        when(usaTaxService.getCountryCode()).thenReturn(CountryCode.US);

        defaultTaxService = mock(DefaultTaxService.class);

        List<TaxService> services = List.of(indiaTaxService, usaTaxService);
        taxServiceProvider = new TaxServiceProvider(services, defaultTaxService);
    }

    @Test
    void shouldReturnIndiaTaxServiceWhenCountryIsIND() {
        TaxService result = taxServiceProvider.getService(CountryCode.IN);
        assertSame(indiaTaxService, result, "Expected IndiaTaxService for IND");
    }

    @Test
    void shouldReturnUsaTaxServiceWhenCountryIsUSA() {
        TaxService result = taxServiceProvider.getService(CountryCode.US);
        assertSame(usaTaxService, result, "Expected UsaTaxService for USA");
    }

    @Test
    void shouldReturnDefaultServiceWhenCountryIsNotConfigured() {
        TaxService result = taxServiceProvider.getService(CountryCode.UK);
        assertSame(defaultTaxService, result, "Expected DefaultTaxService for unconfigured country CAN");
    }
}
