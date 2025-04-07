package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.model.InvoiceStatus;
import com.ibs_demo.invoice_service.request.InvoiceRequest;
import com.ibs_demo.invoice_service.response.InvoiceResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoiceService {
    InvoiceResponse generateInvoice(InvoiceRequest invoiceRequest);
    InvoiceResponse getInvoiceByBillingId(String email, String billingId);
    Page<InvoiceResponse> getInvoicesForUser(String email, int page, int size);
    void softDeleteInvoice(Long invoiceId);
    Page<InvoiceResponse> getFilteredInvoices(String buyerEmail, String supplierEmail, InvoiceStatus status, int page, int size);
}
