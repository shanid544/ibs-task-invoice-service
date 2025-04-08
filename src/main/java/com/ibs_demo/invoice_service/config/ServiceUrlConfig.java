package com.ibs_demo.invoice_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
@Getter
@Setter
public class ServiceUrlConfig {

    private String notificationServiceUrl;
    private String itemServiceUrl;

}
