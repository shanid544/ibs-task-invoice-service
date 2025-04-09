package com.ibs_demo.invoice_service.service.handler;

import com.ibs_demo.invoice_service.inmemory.ItemDataStore;
import com.ibs_demo.invoice_service.response.ItemDetails;
import com.ibs_demo.invoice_service.response.ItemList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemServiceFallbackHandler {


    private final ItemDataStore itemDataStore;

    public ItemList getItemDetails(List<Long> itemIds) {
        List<ItemDetails> cachedItems = itemDataStore.getItemsByIds(itemIds);
        log.info("Returning {} hardcoded items from fallback cache.", cachedItems.size());
        return new ItemList(cachedItems);
    }
}
