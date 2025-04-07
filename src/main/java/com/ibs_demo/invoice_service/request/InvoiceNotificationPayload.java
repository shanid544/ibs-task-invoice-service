package com.ibs_demo.invoice_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceNotificationPayload {
    private String billingId;
    private String buyerEmail;
    private String supplierEmail;
    private String currencyCode;
    private Double totalAmount;
    private Double tax;
    private LocalDate invoiceDate;
    private LocalDate paymentDueDate;
    private List<BillingLineNotificationPayload> billingLines;
}
