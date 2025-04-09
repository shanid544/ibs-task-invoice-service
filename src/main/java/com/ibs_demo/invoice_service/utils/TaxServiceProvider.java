package com.ibs_demo.invoice_service.utils;

import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.service.TaxService;
import com.ibs_demo.invoice_service.service.impl.DefaultTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class TaxServiceProvider {

    private final Map<CountryCode, TaxService> serviceMap = new EnumMap<>(CountryCode.class);
    private final DefaultTaxService defaultTaxService;

    @Autowired
    public TaxServiceProvider(List<TaxService> services, DefaultTaxService defaultTaxService) {
        this.defaultTaxService = defaultTaxService;
        for (TaxService service : services) {
            if (service.getCountryCode() != null) {
                serviceMap.put(service.getCountryCode(), service);
            }
        }
    }

    public TaxService getService(CountryCode countryCode) {
        return serviceMap.getOrDefault(countryCode, defaultTaxService);
    }
}