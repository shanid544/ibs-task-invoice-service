package com.ibs_demo.invoice_service.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "invoice-service")
public class InvoiceServiceConfig {
    private int paymentDueDays;
    private String currencyCode;
    private String invoiceLanguageCode;
    private Double paymentDuePercent;
    private String countryCode;
}
