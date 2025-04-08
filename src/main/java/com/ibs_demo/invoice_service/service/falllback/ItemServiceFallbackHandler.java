package com.ibs_demo.invoice_service.service.falllback;

import com.ibs_demo.invoice_service.cachedata.ItemDataStore;
import com.ibs_demo.invoice_service.response.ItemDetails;
import com.ibs_demo.invoice_service.response.ItemList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

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
