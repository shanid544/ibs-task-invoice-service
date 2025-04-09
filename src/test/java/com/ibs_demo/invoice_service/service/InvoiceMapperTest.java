package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.entity.BillingLine;
import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.entity.PaymentInformation;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.model.Role;
import com.ibs_demo.invoice_service.request.BillingLineNotificationPayload;
import com.ibs_demo.invoice_service.request.InvoiceNotificationPayload;
import com.ibs_demo.invoice_service.response.InvoiceResponse;
import com.ibs_demo.invoice_service.utils.InvoiceMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InvoiceMapperTest {

    @Test
    void testToInvoiceResponse() {
        // Setup invoice
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setBillingId("INV12345");
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPaymentDueDate(LocalDate.now().plusDays(15));
        invoice.setCurrencyCode("INR");
        invoice.setTotalInvoiceAmount(1000.00);
        invoice.setTotalInvoiceAmountDue(1000.00);
        invoice.setInvoiceLanguageCode("EN");
        invoice.setCountryCode(CountryCode.IN);
        invoice.setTax(100.00);

        User buyer = new User(1L, "buyer@example.com", "Buyer Name", "9876543210", "password", Role.BUYER, CountryCode.IN,"ACTIVE" );
        User supplier = new User(2L, "supplier@example.com", "Supplier Name", "8765432109", "password", Role.SUPPLIER, CountryCode.IN, "ACTIVE");
        invoice.setBuyer(buyer);
        invoice.setSupplier(supplier);

        BillingLine billingLine = new BillingLine();
        billingLine.setId(1L);
        billingLine.setItemDescription("Product 1");
        billingLine.setQuantity(2);
        billingLine.setUnitPrice(1000.00);
        billingLine.setTotalAmount(1000.00);

        invoice.setBillingLines(List.of(billingLine));

        PaymentInformation pi = new PaymentInformation();
        pi.setPaymentMethod("Card");
        pi.setCardType("Visa");
        pi.setPaymentInstrumentId("PID123");
        pi.setFormOfPaymentId("FOP123");
        pi.setCardNumberType("Credit");
        pi.setShortCardNum("XXXX-1234");
        invoice.setPaymentInformation(pi);

        // Execute
        InvoiceResponse response = InvoiceMapper.toInvoiceResponse(invoice);

        // Assert
        assertNotNull(response);
        assertEquals("INV12345", response.getBillingId());
        assertEquals("buyer@example.com", response.getBuyer().getEmail());
        assertEquals("supplier@example.com", response.getSupplier().getEmail());
        assertEquals("INR", response.getCurrencyCode());
        assertEquals(1, response.getBillingLines().size());
        assertNotNull(response.getPaymentInformation());
    }

    @Test
    void testToNotificationPayload() {
        Invoice invoice = new Invoice();
        invoice.setBillingId("INV999");
        invoice.setInvoiceDate(LocalDate.of(2024, 12, 1));
        invoice.setPaymentDueDate(LocalDate.of(2025, 1, 1));
        invoice.setCurrencyCode("USD");
        invoice.setTotalInvoiceAmount(1200.00);
        invoice.setTax(120.00);

        User buyer = new User();
        buyer.setEmail("buyer@mail.com");
        invoice.setBuyer(buyer);

        User supplier = new User();
        supplier.setEmail("supplier@mail.com");
        invoice.setSupplier(supplier);

        BillingLine billingLine = new BillingLine();
        billingLine.setItemDescription("Test Item");
        billingLine.setQuantity(3);
        billingLine.setUnitPrice(100.00);
        billingLine.setTotalAmount(300.00);

        invoice.setBillingLines(List.of(billingLine));

        // Execute
        InvoiceNotificationPayload payload = InvoiceMapper.toNotificationPayload(invoice);

        // Assertt
        assertNotNull(payload);
        assertEquals("INV999", payload.getBillingId());
        assertEquals("buyer@mail.com", payload.getBuyerEmail());
        assertEquals("supplier@mail.com", payload.getSupplierEmail());
        assertEquals("USD", payload.getCurrencyCode());
        assertEquals(1, payload.getBillingLines().size());

        BillingLineNotificationPayload line = payload.getBillingLines().get(0);
        assertEquals("Test Item", line.getItemDescription());
        assertEquals(3, line.getQuantity());
    }
}
