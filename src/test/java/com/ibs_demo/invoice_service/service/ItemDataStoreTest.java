package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.cachedata.ItemDataStore;
import com.ibs_demo.invoice_service.response.ItemDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDataStoreTest {

    private ItemDataStore itemDataStore;

    @BeforeEach
    void setUp() {
        itemDataStore = new ItemDataStore();
    }

    @Test
    void testGetItemsByValidIds() {
        List<Long> ids = List.of(1L, 2L);
        List<ItemDetails> items = itemDataStore.getItemsByIds(ids);

        assertEquals(2, items.size());

        assertEquals(1L, items.get(0).getId());
        assertEquals("Wireless Mouse", items.get(0).getName());

        assertEquals(2L, items.get(1).getId());
        assertEquals("Mechanical Keyboard", items.get(1).getName());
    }

    @Test
    void testGetItemsByPartialIds() {
        List<Long> ids = List.of(1L, 99L);
        List<ItemDetails> items = itemDataStore.getItemsByIds(ids);

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void testGetItemsByInvalidIds() {
        List<Long> ids = List.of(99L, 100L);
        List<ItemDetails> items = itemDataStore.getItemsByIds(ids);

        assertTrue(items.isEmpty());
    }
}
