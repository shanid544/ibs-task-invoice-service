package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.entity.BillingLine;
import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.entity.PaymentInformation;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.model.InvoiceStatus;
import com.ibs_demo.invoice_service.model.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testUserEntity() {
        User user = new User();
        user.setId(1L);
        user.setEmail("supplier@example.com");
        user.setName("Supplier A");
        user.setPhoneNumber("1234567890");
        user.setPassword("password");
        user.setRole(Role.SUPPLIER);
        user.setCountryCode(CountryCode.IN);
        user.setStatus("ACTIVE");

        assertEquals(1L, user.getId());
        assertEquals("supplier@example.com", user.getEmail());
        assertEquals("Supplier A", user.getName());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals("password", user.getPassword());
        assertEquals(Role.SUPPLIER, user.getRole());
        assertEquals(CountryCode.IN, user.getCountryCode());
        assertEquals("ACTIVE", user.getStatus());
    }

    @Test
    void testPaymentInformationEmbeddable() {
        PaymentInformation info = new PaymentInformation();
        info.setPaymentMethod("CREDIT_CARD");
        info.setCardType("VISA");
        info.setPaymentInstrumentId("PAY123");
        info.setFormOfPaymentId("FORM456");
        info.setCardNumberType("4111XXXXXXXX1234");
        info.setShortCardNum("1234");

        assertEquals("CREDIT_CARD", info.getPaymentMethod());
        assertEquals("VISA", info.getCardType());
        assertEquals("PAY123", info.getPaymentInstrumentId());
        assertEquals("FORM456", info.getFormOfPaymentId());
        assertEquals("4111XXXXXXXX1234", info.getCardNumberType());
        assertEquals("1234", info.getShortCardNum());
    }

    @Test
    void testInvoiceEntity() {
        User supplier = new User(1L, "supplier@example.com", "Supplier", "1234567890", "pass", Role.SUPPLIER, CountryCode.IN, "ACTIVE");
        User buyer = new User(2L, "buyer@example.com", "Buyer", "9876543210", "pass", Role.BUYER, CountryCode.US, "ACTIVE");

        PaymentInformation paymentInfo = new PaymentInformation(
                "PAYPAL", "N/A", "PI123", "FP456", null, null
        );

        Invoice invoice = new Invoice();
        invoice.setId(100L);
        invoice.setBillingId("BILL-001");
        invoice.setSupplier(supplier);
        invoice.setBuyer(buyer);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPaymentDueDate(LocalDate.now().plusDays(7));
        invoice.setCurrencyCode("USD");
        invoice.setTotalInvoiceAmount(1000.0);
        invoice.setTotalInvoiceAmountDue(500.0);
        invoice.setPaymentInformation(paymentInfo);
        invoice.setInvoiceLanguageCode("en");
        invoice.setCountryCode(CountryCode.US);
        invoice.setTax(10.0);
        invoice.setStatus(InvoiceStatus.PAID);

        assertEquals("BILL-001", invoice.getBillingId());
        assertEquals("USD", invoice.getCurrencyCode());
        assertEquals(paymentInfo, invoice.getPaymentInformation());
        assertEquals(InvoiceStatus.PAID, invoice.getStatus());
    }

    @Test
    void testBillingLineEntity() {
        Invoice invoice = new Invoice();
        invoice.setId(101L);
        invoice.setBillingId("BILL-002");

        BillingLine line = new BillingLine();
        line.setId(1L);
        line.setInvoice(invoice);
        line.setItemDescription("Item A");
        line.setQuantity(2);
        line.setUnitPrice(50.0);
        line.setTotalAmount(100.0);

        assertEquals(1L, line.getId());
        assertEquals(invoice, line.getInvoice());
        assertEquals("Item A", line.getItemDescription());
        assertEquals(2, line.getQuantity());
        assertEquals(50.0, line.getUnitPrice());
        assertEquals(100.0, line.getTotalAmount());
    }

    @Test
    void testInvoiceAndBillingLinesAssociation() {
        Invoice invoice = new Invoice();
        invoice.setBillingId("BILL-003");

        BillingLine line1 = new BillingLine(null, "Item 1", 1, 100.0, 100.0);
        BillingLine line2 = new BillingLine(null, "Item 2", 2, 150.0, 300.0);

        line1.setInvoice(invoice);
        line2.setInvoice(invoice);

        invoice.setBillingLines(List.of(line1, line2));

        assertEquals(2, invoice.getBillingLines().size());
        assertEquals(invoice, invoice.getBillingLines().get(0).getInvoice());
    }
}
