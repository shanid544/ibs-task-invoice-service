package com.ibs_demo.invoice_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingLineResponse {
    private Long id;
    private String itemDescription;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
}
