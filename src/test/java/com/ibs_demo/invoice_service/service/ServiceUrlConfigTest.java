package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.config.ServiceUrlConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceUrlConfigTest {

    @Test
    void testServiceUrlConfigSettersAndGetters() {
        ServiceUrlConfig config = new ServiceUrlConfig();
        config.setNotificationServiceUrl("http://localhost:8081");
        config.setItemServiceUrl("http://localhost:8082");

        assertEquals("http://localhost:8081", config.getNotificationServiceUrl());
        assertEquals("http://localhost:8082", config.getItemServiceUrl());
    }
}
