package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.response.ItemList;
import com.ibs_demo.invoice_service.service.fallback.ItemServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "item-service", url = "http://localhost:9091", fallback = ItemServiceFallback.class)
public interface ItemServiceClient {

    @PostMapping("/api/items/list")
    ItemList getItemDetails(@RequestBody List<Long> itemIds);
}