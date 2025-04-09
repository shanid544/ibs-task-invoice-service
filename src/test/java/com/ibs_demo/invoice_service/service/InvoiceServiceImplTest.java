package com.ibs_demo.invoice_service.service;


import com.ibs_demo.invoice_service.config.InvoiceServiceConfig;
import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.event.InvoiceCreatedEvent;
import com.ibs_demo.invoice_service.exception.appexceptions.*;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.repository.InvoiceRepository;
import com.ibs_demo.invoice_service.repository.UserRepository;
import com.ibs_demo.invoice_service.request.BillingLineRequest;
import com.ibs_demo.invoice_service.request.InvoiceRequest;
import com.ibs_demo.invoice_service.request.PaymentInformationRequest;
import com.ibs_demo.invoice_service.response.InvoiceResponse;
import com.ibs_demo.invoice_service.response.ItemDetails;
import com.ibs_demo.invoice_service.response.ItemList;
import com.ibs_demo.invoice_service.service.impl.InvoiceServiceImpl;
import com.ibs_demo.invoice_service.utils.TaxServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemServiceRestClient itemServiceClient;

    @Mock
    private InvoiceServiceConfig invoiceServiceConfig;

    @Mock
    private TaxServiceProvider taxServiceProvider;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private TaxService taxService;

    @BeforeEach
    void setupSecurityContext() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_SUPPLIER"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("supplier@example.com", null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(invoiceServiceConfig.getCurrencyCode()).thenReturn("USD");
        when(invoiceServiceConfig.getInvoiceLanguageCode()).thenReturn("EN");
        when(invoiceServiceConfig.getCountryCode()).thenReturn("IN");
        when(invoiceServiceConfig.getPaymentDueDays()).thenReturn(7);
        when(invoiceServiceConfig.getPaymentDuePercent()).thenReturn(0.1);
    }

    @Test
    void testGenerateInvoice_success() {
        String supplierEmail = "supplier@example.com";
        User supplier = new User();
        supplier.setEmail(supplierEmail);
        supplier.setCountryCode(CountryCode.IN);

        User buyer = new User();
        buyer.setId(1L);
        buyer.setEmail("buyer@example.com");

        when(userRepository.findByEmail(supplierEmail)).thenReturn(Optional.of(supplier));
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));

        List<Long> itemIds = List.of(1L);
        ItemDetails itemDetails = new ItemDetails(1L, "Item A", "Some description", 100.0);
        ItemList itemList = new ItemList(List.of(itemDetails));
        when(itemServiceClient.getItemDetails(itemIds)).thenReturn(itemList);

        when(taxServiceProvider.getService(CountryCode.IN)).thenReturn(taxService);
        when(taxService.calculateTax(100.0)).thenReturn(10.0);

        Invoice invoiceMock = new Invoice();
        invoiceMock.setBillingId("BILL-001");
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoiceMock);

        // Prepare request
        InvoiceRequest request = new InvoiceRequest();
        request.setBillingId("BILL-001");
        request.setBuyerId(1L);

        BillingLineRequest billingLineRequest = new BillingLineRequest();
        billingLineRequest.setItemId(1L);
        billingLineRequest.setQuantity(1);

        request.setBillingLines(List.of(billingLineRequest));
        request.setPaymentInformation(new PaymentInformationRequest());

        // Call method
        InvoiceResponse response = invoiceService.generateInvoice(request);

        // Verify
        assertNotNull(response);
        assertEquals("BILL-001", response.getBillingId());
        verify(invoiceRepository).save(any(Invoice.class));
        verify(applicationEventPublisher).publishEvent(any(InvoiceCreatedEvent.class));
    }

    @Test
    void testGenerateInvoice_supplierNotFound() {
        // Given
        String supplierEmail = "supplier@example.com";
        // Assume: SecurityUtils.getCurrentUserEmail() will return this from SecurityContext

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setBillingId("BILL-001");
        invoiceRequest.setBuyerId(1L);
        invoiceRequest.setBillingLines(List.of(
                new BillingLineRequest(1L, 2)
        ));

        when(userRepository.findByEmail(supplierEmail)).thenReturn(Optional.empty());

        // When & Then
        SupplierNotFoundException exception = assertThrows(SupplierNotFoundException.class, () -> {
            invoiceService.generateInvoice(invoiceRequest);
        });

        assertEquals("Supplier not found with email: "+supplierEmail, exception.getMessage());
    }

    @Test
    void testGenerateInvoice_buyerNotFound() {
        // Arrange
        String supplierEmail = "supplier@example.com";
        Long buyerId = 2L;

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setBillingId("BILL-002");
        invoiceRequest.setBuyerId(buyerId);
        invoiceRequest.setBillingLines(List.of(new BillingLineRequest(1L, 3)));

        User supplier = new User();
        supplier.setEmail(supplierEmail);
        supplier.setCountryCode(CountryCode.IN); // Or null if you want to test fallback config

        when(userRepository.findByEmail(supplierEmail)).thenReturn(Optional.of(supplier));
        when(userRepository.findById(buyerId)).thenReturn(Optional.empty());

        // Act & Assert
        BuyerNotFoundException exception = assertThrows(BuyerNotFoundException.class, () -> {
            invoiceService.generateInvoice(invoiceRequest);
        });

        assertEquals("Buyer not found with ID: "+buyerId, exception.getMessage());
    }
    @Test
    void testGenerateInvoice_itemQuantityIsNull() {
        // Arrange
        String supplierEmail = "supplier@example.com";

        User supplier = new User();
        supplier.setEmail(supplierEmail);
        supplier.setCountryCode(CountryCode.IN);

        User buyer = new User();
        buyer.setId(2L);
        buyer.setEmail("buyer@example.com");

        when(userRepository.findByEmail(supplierEmail)).thenReturn(Optional.of(supplier));
        when(userRepository.findById(2L)).thenReturn(Optional.of(buyer));

        // Create BillingLineRequest with null quantity
        BillingLineRequest billingLineRequest = new BillingLineRequest();
        billingLineRequest.setItemId(1L);
        billingLineRequest.setQuantity(null); // Simulating null quantity

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setBillingId("INV-002");
        invoiceRequest.setBuyerId(2L);
        invoiceRequest.setBillingLines(List.of(billingLineRequest));
        invoiceRequest.setPaymentInformation(new PaymentInformationRequest());

        // Mock item details
        ItemDetails itemDetails = new ItemDetails(1L, "Null Quantity Item", "Should fail", 200.0);
        when(itemServiceClient.getItemDetails(List.of(1L)))
                .thenReturn(new ItemList(List.of(itemDetails)));

        // Act & Assert
        MissingItemQuantityException exception = assertThrows(MissingItemQuantityException.class,
                () -> invoiceService.generateInvoice(invoiceRequest));

        assertTrue(exception.getMessage().contains("1"), "Exception message should contain the missing item ID");
    }

    @Test
    void testGenerateInvoice_invalidCountryCode() {
        // Arrange
        InvoiceRequest invoiceRequest = createValidInvoiceRequest();
        invoiceRequest.setBillingId("INV-004");

        // Simulate a supplier with no country code
        User supplier = new User();
        supplier.setEmail("supplier@example.com");
        supplier.setCountryCode(null); // No country code set

        when(userRepository.findByEmail("supplier@example.com")).thenReturn(Optional.of(supplier));

        // Simulate a valid buyer
        User buyer = new User();
        buyer.setId(2L);
        buyer.setEmail("buyer@example.com");
        when(userRepository.findById(2L)).thenReturn(Optional.of(buyer));

        // Set invalid country code in config
        when(invoiceServiceConfig.getCountryCode()).thenReturn("INVALID_CODE");

        // Mock ItemService to return valid item details
        ItemDetails itemDetails = new ItemDetails(1L, "Item A", "Test", 100.0);
        when(itemServiceClient.getItemDetails(List.of(1L)))
                .thenReturn(new ItemList(List.of(itemDetails)));



        // Act & Assert
        InvalidCountryCodeException exception = assertThrows(InvalidCountryCodeException.class,
                () -> invoiceService.generateInvoice(invoiceRequest));

        assertTrue(exception.getMessage().contains("Invalid country code configured: "));
    }

    @Test
    void testGenerateInvoice_nullCountryCode() {
        // Arrange
        InvoiceRequest invoiceRequest = createValidInvoiceRequest();
        invoiceRequest.setBillingId("INV-004");

        // Simulate a supplier with no country code
        User supplier = new User();
        supplier.setEmail("supplier@example.com");
        supplier.setCountryCode(null); // No country code set

        when(userRepository.findByEmail("supplier@example.com")).thenReturn(Optional.of(supplier));

        // Simulate a valid buyer
        User buyer = new User();
        buyer.setId(2L);
        buyer.setEmail("buyer@example.com");
        when(userRepository.findById(2L)).thenReturn(Optional.of(buyer));

        // Set invalid country code in config
        when(invoiceServiceConfig.getCountryCode()).thenReturn(null);

        // Mock ItemService to return valid item details
        ItemDetails itemDetails = new ItemDetails(1L, "Item A", "Test", 100.0);
        when(itemServiceClient.getItemDetails(List.of(1L)))
                .thenReturn(new ItemList(List.of(itemDetails)));



        // Act & Assert
        CountryCodeNotConfiguredException exception = assertThrows(CountryCodeNotConfiguredException.class,
                () -> invoiceService.generateInvoice(invoiceRequest));

        assertTrue(exception.getMessage().contains("invoice-service.country-code"));
    }

    private InvoiceRequest createValidInvoiceRequest() {
        BillingLineRequest line = new BillingLineRequest();
        line.setItemId(1L);
        line.setQuantity(2);

        InvoiceRequest request = new InvoiceRequest();
        request.setBillingId("INV-TEST");
        request.setBuyerId(2L);
        request.setBillingLines(List.of(line));

        PaymentInformationRequest paymentInfo = new PaymentInformationRequest();
        paymentInfo.setPaymentMethod("CARD");
        request.setPaymentInformation(paymentInfo);

        return request;
    }



    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }


}
