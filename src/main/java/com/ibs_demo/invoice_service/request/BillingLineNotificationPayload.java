package com.ibs_demo.invoice_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingLineNotificationPayload {
    private String itemDescription;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
}
