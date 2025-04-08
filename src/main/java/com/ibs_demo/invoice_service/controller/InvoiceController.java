package com.ibs_demo.invoice_service.controller;

import com.ibs_demo.invoice_service.model.InvoiceStatus;
import com.ibs_demo.invoice_service.request.InvoiceRequest;
import com.ibs_demo.invoice_service.response.InvoiceResponse;
import com.ibs_demo.invoice_service.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PreAuthorize("hasRole('SUPPLIER')")
    @PostMapping("/generate")
    public ResponseEntity<InvoiceResponse> generateInvoice(@RequestBody @Valid InvoiceRequest invoiceRequest) {
        return ResponseEntity.ok(invoiceService.generateInvoice(invoiceRequest));
    }

    @GetMapping("/{email}/{billingId}")
    public ResponseEntity<InvoiceResponse> getInvoiceByBillingId(@PathVariable String email, @PathVariable String billingId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByBillingId(email, billingId));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Page<InvoiceResponse>> getInvoicesForUser(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(invoiceService.getInvoicesForUser(email, page, size));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('BUYER', 'SUPPLIER')")
    public ResponseEntity<Page<InvoiceResponse>> filterInvoices(
            @RequestParam(required = false) String buyerEmail,
            @RequestParam(required = false) String supplierEmail,
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<InvoiceResponse> invoices = invoiceService.getFilteredInvoices(buyerEmail, supplierEmail, status, page, size);
        return ResponseEntity.ok(invoices);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteInvoice(@PathVariable Long id) {
        invoiceService.softDeleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
