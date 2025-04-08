package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.response.ItemList;

import java.util.List;

public interface ItemServiceRestClient {
    ItemList getItemDetails(List<Long> itemIds);
}
