package com.ibs_demo.invoice_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "services")
@Getter
@Setter
public class ServiceUrlConfig {

    private String notificationServiceUrl;

}
