package com.ibs_demo.invoice_service.service.service;

import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.exception.appexceptions.*;
import com.ibs_demo.invoice_service.model.InvoiceStatus;
import com.ibs_demo.invoice_service.model.Role;
import com.ibs_demo.invoice_service.repository.InvoiceRepository;
import com.ibs_demo.invoice_service.repository.UserRepository;
import com.ibs_demo.invoice_service.response.InvoiceResponse;
import com.ibs_demo.invoice_service.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceRetrievalServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice invoice;
    private User buyer;
    private User supplier;



    @BeforeEach
    void setUp() {
        buyer = new User();
        buyer.setEmail("buyer@example.com");
        buyer.setRole(Role.BUYER);

        supplier = new User();
        supplier.setEmail("supplier@example.com");
        supplier.setRole(Role.SUPPLIER);

        invoice = new Invoice();
        invoice.setBillingId("INV-001");
        invoice.setBuyer(buyer);
        invoice.setSupplier(supplier);
    }

    @Test
    void testGetInvoiceByBillingId_asBuyer_success() {
        when(invoiceRepository.findByBillingId("INV-001")).thenReturn(Optional.of(invoice));

        InvoiceResponse response = invoiceService.getInvoiceByBillingId("buyer@example.com", "INV-001");

        assertNotNull(response);
        verify(invoiceRepository).findByBillingId("INV-001");
    }

    @Test
    void testGetInvoiceByBillingId_asSupplier_success() {
        when(invoiceRepository.findByBillingId("INV-001")).thenReturn(Optional.of(invoice));

        InvoiceResponse response = invoiceService.getInvoiceByBillingId("supplier@example.com", "INV-001");

        assertNotNull(response);
        verify(invoiceRepository).findByBillingId("INV-001");
    }

    @Test
    void testGetInvoiceByBillingId_accessDenied() {
        when(invoiceRepository.findByBillingId("INV-001")).thenReturn(Optional.of(invoice));

        InvoiceAccessDeniedException ex = assertThrows(InvoiceAccessDeniedException.class,
                () -> invoiceService.getInvoiceByBillingId("unauthorized@example.com", "INV-001"));

        assertEquals("Access Denied", ex.getMessage());
        verify(invoiceRepository).findByBillingId("INV-001");
    }

    @Test
    void testGetInvoiceByBillingId_invoiceNotFound() {
        when(invoiceRepository.findByBillingId("INV-999")).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class,
                () -> invoiceService.getInvoiceByBillingId("buyer@example.com", "INV-999"));

        verify(invoiceRepository).findByBillingId("INV-999");
    }


    @Test
    void testGetInvoicesForUser_asBuyer_success() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("invoiceDate").descending());
        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice), pageable, 1);

        when(userRepository.findByEmail("buyer@example.com")).thenReturn(Optional.of(buyer));
        when(invoiceRepository.findByBuyer(buyer, pageable)).thenReturn(invoicePage);

        Page<InvoiceResponse> result = invoiceService.getInvoicesForUser("buyer@example.com", 0, 10);

        assertEquals(1, result.getTotalElements());
        assertNotNull(result.getContent());
        verify(userRepository).findByEmail("buyer@example.com");
        verify(invoiceRepository).findByBuyer(buyer, pageable);
    }

    @Test
    void testGetInvoicesForUser_asSupplier_success() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("invoiceDate").descending());
        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice), pageable, 1);

        when(userRepository.findByEmail("supplier@example.com")).thenReturn(Optional.of(supplier));
        when(invoiceRepository.findBySupplier(supplier, pageable)).thenReturn(invoicePage);

        Page<InvoiceResponse> result = invoiceService.getInvoicesForUser("supplier@example.com", 0, 10);

        assertEquals(1, result.getTotalElements());
        assertNotNull(result.getContent());
        verify(userRepository).findByEmail("supplier@example.com");
        verify(invoiceRepository).findBySupplier(supplier, pageable);
    }

    @Test
    void testGetInvoicesForUser_userNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> invoiceService.getInvoicesForUser("unknown@example.com", 0, 10));

        verify(userRepository).findByEmail("unknown@example.com");
    }

    @Test
    void testGetFilteredInvoices_success() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("invoiceDate").descending());
        invoice.setStatus(InvoiceStatus.PAID);
        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice), pageable, 1);

        when(invoiceRepository.findFilteredInvoices("buyer@example.com", "supplier@example.com", InvoiceStatus.PAID, pageable))
                .thenReturn(invoicePage);

        Page<InvoiceResponse> result = invoiceService.getFilteredInvoices("buyer@example.com", "supplier@example.com", InvoiceStatus.PAID, 0, 10);

        assertEquals(1, result.getTotalElements());
        assertNotNull(result.getContent());
        verify(invoiceRepository).findFilteredInvoices("buyer@example.com", "supplier@example.com", InvoiceStatus.PAID, pageable);
    }

    @Test
    void testGetFilteredInvoices_emptyResult() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("invoiceDate").descending());
        Page<Invoice> invoicePage = new PageImpl<>(List.of(), pageable, 0);

        when(invoiceRepository.findFilteredInvoices(null, null, null, pageable)).thenReturn(invoicePage);

        Page<InvoiceResponse> result = invoiceService.getFilteredInvoices(null, null, null, 0, 10);

        assertEquals(0, result.getTotalElements());
        verify(invoiceRepository).findFilteredInvoices(null, null, null, pageable);
    }

    @Test
    void testSoftDeleteInvoice_success() {
        Invoice invoiceOne = new Invoice();
        invoiceOne.setId(1L);
        invoiceOne.setStatus(InvoiceStatus.ACTIVE);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoiceOne));

        invoiceService.softDeleteInvoice(1L);

        assertEquals(InvoiceStatus.INACTIVE, invoiceOne.getStatus());
        verify(invoiceRepository).save(invoiceOne);
    }

    @Test
    void testSoftDeleteInvoice_invoiceNotFound() {
        when(invoiceRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> invoiceService.softDeleteInvoice(999L));

        assertEquals("Invoice not found with ID: 999", ex.getMessage());
    }

    @Test
    void testSoftDeleteInvoice_alreadyInactive() {
        Invoice invoiceOne = new Invoice();
        invoiceOne.setId(1L);
        invoiceOne.setStatus(InvoiceStatus.INACTIVE);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoiceOne));

        InactiveInvoiceException ex = assertThrows(InactiveInvoiceException.class,
                () -> invoiceService.softDeleteInvoice(1L));

        assertEquals("Invoice with billing ID '1' is inactive.", ex.getMessage());
    }

    @Test
    void testSoftDeleteInvoice_alreadyPaid() {
        Invoice invoiceOne = new Invoice();
        invoiceOne.setId(1L);
        invoiceOne.setStatus(InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoiceOne));

        AlreadyPaidInvoiceException ex = assertThrows(AlreadyPaidInvoiceException.class,
                () -> invoiceService.softDeleteInvoice(1L));

        assertEquals("Invoice with billing ID '1' is already paid.", ex.getMessage());
    }




}