package com.ibs_demo.invoice_service.service.impl;

import com.ibs_demo.invoice_service.config.ServiceUrlConfig;
import com.ibs_demo.invoice_service.response.ItemList;
import com.ibs_demo.invoice_service.service.ItemServiceRestClient;
import com.ibs_demo.invoice_service.service.handler.ItemServiceFallbackHandler;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceRestClientImpl implements ItemServiceRestClient {

    private final RestTemplate restTemplate;
    private final ServiceUrlConfig serviceUrlConfig;
    private final ItemServiceFallbackHandler itemServiceFallbackHandler;

    @Override
    @CircuitBreaker(name = "itemService", fallbackMethod = "getItemDetailsFallback")
    @Retry(name = "itemService")
    public ItemList getItemDetails(List<Long> itemIds) {
        long startTime = System.nanoTime();

        try {
            String url = serviceUrlConfig.getItemServiceUrl() + "/api/items/list";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<Long>> request = new HttpEntity<>(itemIds, headers);

            ResponseEntity<ItemList> response = restTemplate.postForEntity(url, request, ItemList.class);
            return response.getBody();

        } finally {
            long endTime = System.nanoTime();
            double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;
            log.info("Request to Item Service attempted for {} seconds", elapsedSeconds);
        }
    }


    public ItemList getItemDetailsFallback(List<Long> itemIds, Throwable t) {
        if (t instanceof ResourceAccessException) {
            Throwable cause = t.getCause();
            if (cause instanceof java.net.SocketTimeoutException) {
                log.error("Read Timeout: Failed to get response from Item Service within the expected time. Cause: {}", cause.getMessage());
            } else if (cause instanceof java.net.ConnectException) {
                log.error("Connection Timeout: Unable to establish connection with Item Service. Cause: {}", cause.getMessage());
            } else {
                log.error("Resource Access Exception when calling Item Service. Cause: {}", cause.getMessage());
            }
        } else {
            log.error("Fallback triggered for Item Service due to unexpected error: {}", t.getMessage(), t);
        }
        log.info("Returning hardcoded items from fallback cache.");
        return itemServiceFallbackHandler.getItemDetails(itemIds);
    }

}
