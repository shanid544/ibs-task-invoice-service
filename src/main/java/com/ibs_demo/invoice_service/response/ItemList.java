package com.ibs_demo.invoice_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemList {

    private List<ItemDetails> itemDetailsList;
}
