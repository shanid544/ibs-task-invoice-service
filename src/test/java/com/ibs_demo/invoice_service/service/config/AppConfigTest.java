package com.ibs_demo.invoice_service.service.config;

import com.ibs_demo.invoice_service.config.resttemplate.AppConfig;
import com.ibs_demo.invoice_service.config.resttemplate.CustomClientHttpRequestInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    void testClientHttpRequestFactory() {
        ClientHttpRequestFactory factory = appConfigTestHelper(); // calling private via reflection
        assertNotNull(factory);
        assertTrue(factory instanceof HttpComponentsClientHttpRequestFactory);
    }

    @Test
    void testRestTemplateBean() {
        RestTemplate restTemplate = appConfig.restTemplate();

        assertNotNull(restTemplate);
        assertNotNull(restTemplate.getRequestFactory());
        assertEquals(1, restTemplate.getInterceptors().size());
        assertTrue(restTemplate.getInterceptors().get(0) instanceof CustomClientHttpRequestInterceptor);
    }

    // Helper method to test private method via reflection
    private ClientHttpRequestFactory appConfigTestHelper() {
        try {
            var method = AppConfig.class.getDeclaredMethod("clientHttpRequestFactory");
            method.setAccessible(true);
            return (ClientHttpRequestFactory) method.invoke(appConfig);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke clientHttpRequestFactory", e);
        }
    }
}
