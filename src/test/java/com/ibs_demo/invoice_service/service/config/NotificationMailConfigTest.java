package com.ibs_demo.invoice_service.service.config;

import com.ibs_demo.invoice_service.config.NotificationMailConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({})
class NotificationMailConfigTest {

    @Test
    void testVersionGetterAndSetter() {
        NotificationMailConfig config = new NotificationMailConfig();
        config.setVersion("v2");

        assertEquals("v2", config.getVersion());
    }
}
