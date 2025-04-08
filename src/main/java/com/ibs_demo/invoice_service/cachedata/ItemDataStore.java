package com.ibs_demo.invoice_service.cachedata;

import com.ibs_demo.invoice_service.response.ItemDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

@Component
public class ItemDataStore {

    private final Map<Long, ItemDetails> hardcodedItems = new HashMap<>();

    public ItemDataStore() {
        hardcodedItems.put(1L, new ItemDetails(1L, "Wireless Mouse", "Ergonomic wireless mouse with 3 DPI settings", 25.99));
        hardcodedItems.put(2L, new ItemDetails(2L, "Mechanical Keyboard", "RGB mechanical keyboard with blue switches", 59.49));
    }

    public List<ItemDetails> getItemsByIds(List<Long> ids) {
        return ids.stream()
                .map(hardcodedItems::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
