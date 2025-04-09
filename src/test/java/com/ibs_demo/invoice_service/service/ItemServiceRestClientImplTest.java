package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.config.ServiceUrlConfig;
import com.ibs_demo.invoice_service.response.ItemList;
import com.ibs_demo.invoice_service.service.handler.ItemServiceFallbackHandler;
import com.ibs_demo.invoice_service.service.impl.ItemServiceRestClientImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ItemServiceRestClientImplTest {

    @Test
    void testGetItemDetails_success() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        ServiceUrlConfig config = mock(ServiceUrlConfig.class);
        ItemServiceFallbackHandler fallbackHandler = mock(ItemServiceFallbackHandler.class);

        ItemServiceRestClientImpl client = new ItemServiceRestClientImpl(restTemplate, config, fallbackHandler);

        List<Long> itemIds = List.of(1L, 2L);
        String itemServiceUrl = "http://item-service";

        ItemList expected = new ItemList();
        ResponseEntity<ItemList> response = new ResponseEntity<>(expected, HttpStatus.OK);

        when(config.getItemServiceUrl()).thenReturn(itemServiceUrl);
        when(restTemplate.postForEntity(eq(itemServiceUrl + "/api/items/list"), any(HttpEntity.class), eq(ItemList.class))).thenReturn(response);

        ItemList actual = client.getItemDetails(itemIds);

        assertEquals(expected, actual);
        verify(restTemplate).postForEntity(eq(itemServiceUrl + "/api/items/list"), any(HttpEntity.class), eq(ItemList.class));
    }

    @Test
    void testGetItemDetails_fallbackTriggered() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        ServiceUrlConfig config = mock(ServiceUrlConfig.class);
        ItemServiceFallbackHandler fallbackHandler = mock(ItemServiceFallbackHandler.class);

        ItemServiceRestClientImpl client = new ItemServiceRestClientImpl(restTemplate, config, fallbackHandler);

        List<Long> itemIds = List.of(1L, 2L);
        ItemList fallbackResponse = new ItemList();

        when(fallbackHandler.getItemDetails(itemIds)).thenReturn(fallbackResponse);

        ItemList result = client.getItemDetailsFallback(itemIds, new ResourceAccessException("Connection refused", new java.net.ConnectException("Connection refused")));

        assertEquals(fallbackResponse, result);
        verify(fallbackHandler).getItemDetails(itemIds);
    }
}
