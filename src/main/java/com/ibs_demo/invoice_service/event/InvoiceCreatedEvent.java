package com.ibs_demo.invoice_service.event;

import com.ibs_demo.invoice_service.entity.Invoice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceCreatedEvent {
    private final Invoice invoice;

    public InvoiceCreatedEvent(Invoice invoice) {
        this.invoice = invoice;
    }

}
