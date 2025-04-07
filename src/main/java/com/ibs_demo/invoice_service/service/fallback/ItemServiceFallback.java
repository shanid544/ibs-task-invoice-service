package com.ibs_demo.invoice_service.service.fallback;

import com.ibs_demo.invoice_service.response.ItemList;
import com.ibs_demo.invoice_service.service.ItemServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ItemServiceFallback implements ItemServiceClient {
    @Override
    public ItemList getItemDetails(List<Long> itemIds) {
        log.error("Item Service is DOWN! Returning fallback response.");
        return new ItemList(Collections.emptyList());
    }
}
