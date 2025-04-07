package com.ibs_demo.invoice_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetails {
    private Long id;
    private String name;
    private String description;
    private Double unitPrice;
}
