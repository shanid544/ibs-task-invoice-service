package com.ibs_demo.invoice_service.service.impl;

import com.ibs_demo.invoice_service.config.InvoiceServiceConfig;
import com.ibs_demo.invoice_service.entity.BillingLine;
import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.entity.PaymentInformation;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.event.InvoiceCreatedEvent;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.model.InvoiceStatus;
import com.ibs_demo.invoice_service.model.Role;
import com.ibs_demo.invoice_service.repository.BillingLineRepository;
import com.ibs_demo.invoice_service.repository.InvoiceRepository;
import com.ibs_demo.invoice_service.repository.UserRepository;
import com.ibs_demo.invoice_service.request.BillingLineRequest;
import com.ibs_demo.invoice_service.request.InvoiceRequest;
import com.ibs_demo.invoice_service.response.InvoiceResponse;
import com.ibs_demo.invoice_service.response.ItemDetails;
import com.ibs_demo.invoice_service.response.ItemList;
import com.ibs_demo.invoice_service.service.InvoiceService;
import com.ibs_demo.invoice_service.service.ItemServiceClient;
import com.ibs_demo.invoice_service.service.TaxService;
import com.ibs_demo.invoice_service.utils.InvoiceMapper;
import com.ibs_demo.invoice_service.utils.TaxServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final BillingLineRepository billingLineRepository;
    private final UserRepository userRepository;
    private final ItemServiceClient itemServiceClient;
    private final InvoiceServiceConfig invoiceServiceConfig;
    private final TaxServiceProvider taxServiceProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PreAuthorize("hasRole('SUPPLIER')")
    public InvoiceResponse generateInvoice(InvoiceRequest invoiceRequest) {
        log.info("Generating invoice for supplier: {}", invoiceRequest.getSupplierEmail());
        Invoice invoice = buildInvoice(invoiceRequest);

        for (BillingLine billingLine : invoice.getBillingLines()) {
            billingLine.setInvoice(invoice);
        }
        Invoice savedInvoice = invoiceRepository.save(invoice);

        applicationEventPublisher.publishEvent(new InvoiceCreatedEvent(savedInvoice));

        log.info("Invoice generated successfully with ID: {}", savedInvoice.getBillingId());
        return InvoiceMapper.toInvoiceResponse(savedInvoice);
    }

    private Invoice buildInvoice(InvoiceRequest invoiceRequest) {
        User supplier = userRepository.findByEmail(invoiceRequest.getSupplierEmail())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (supplier.getRole() != Role.SUPPLIER) {
            throw new RuntimeException("User is not a supplier");
        }

        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.ACTIVE);
        invoice.setBillingId(invoiceRequest.getBillingId());
        invoice.setSupplier(supplier);
        invoice.setBuyer(userRepository.findById(invoiceRequest.getBuyerId()).orElseThrow(
                () -> new RuntimeException("Supplier not found")
        ));
        List<Long> itemIds = invoiceRequest.getBillingLines()
                .stream()
                .map(BillingLineRequest::getItemId)
                .toList();

        ItemList itemList = getItemDetails(itemIds);

        Map<Long, Integer> itemIdToQuantityMap = invoiceRequest.getBillingLines().stream()
                .collect(Collectors.toMap(BillingLineRequest::getItemId, (BillingLineRequest::getQuantity)));

        CountryCode countryCode = getCountryCode(supplier);
        List<BillingLine> billingLines = getBillingLines(itemList, itemIdToQuantityMap, invoice);

        TaxService taxService = taxServiceProvider.getService(countryCode);
        double baseAmount = invoice.getBillingLines().stream().mapToDouble(BillingLine::getTotalAmount).sum();
        double tax = taxService.calculateTax(baseAmount);
        double finalTotal = baseAmount + tax;

        invoice.setBillingLines(billingLines);
        invoice.setCountryCode(countryCode);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPaymentDueDate(LocalDate.now().plusDays(invoiceServiceConfig.getPaymentDueDays()));
        invoice.setInvoiceLanguageCode(invoiceServiceConfig.getInvoiceLanguageCode());
        invoice.setCurrencyCode(invoiceServiceConfig.getCurrencyCode());
        invoice.setTotalInvoiceAmount(finalTotal);
        invoice.setTax(tax);
        invoice.setTotalInvoiceAmountDue(finalTotal + baseAmount * invoiceServiceConfig.getPaymentDuePercent());
        PaymentInformation paymentInformation = getPaymentInformation(invoiceRequest, invoice);
        invoice.setPaymentInformation(paymentInformation);
        return invoice;
    }

    private static PaymentInformation getPaymentInformation(InvoiceRequest invoiceRequest, Invoice invoice) {
        PaymentInformation paymentInformation = invoice.getPaymentInformation();
        if (paymentInformation == null) {
            paymentInformation = new PaymentInformation();
            invoice.setPaymentInformation(paymentInformation);
        }
        BeanUtils.copyProperties(invoiceRequest.getPaymentInformation(), paymentInformation);
        return paymentInformation;
    }

    private static List<BillingLine> getBillingLines(ItemList itemList, Map<Long, Integer> itemIdToQuantityMap, Invoice invoice) {
        List<BillingLine> billingLines = new ArrayList<>();

        for (ItemDetails item : itemList.getItemDetailsList()) {
            Integer quantity = itemIdToQuantityMap.getOrDefault(item.getId(), null);

            if (quantity == null) {
                throw new RuntimeException("Quantity not found for item ID: " + item.getId());
            }

            BillingLine billingLine = new BillingLine();
            billingLine.setInvoice(invoice);
            billingLine.setQuantity(quantity);
            billingLine.setItemDescription(item.getName());
            billingLine.setUnitPrice(item.getUnitPrice());
            billingLine.setTotalAmount(item.getUnitPrice()*quantity);
            billingLines.add(billingLine);
        }
        return billingLines;
    }

    private CountryCode getCountryCode(User supplier) {
        CountryCode countryCode = supplier.getCountryCode();

        if (countryCode == null) {
            String configCountryCode = invoiceServiceConfig.getCountryCode();

            if (configCountryCode == null || configCountryCode.isBlank()) {
                throw new RuntimeException("Country code not configured. Please set 'invoice-service.country-code' in application properties.");
            }

            try {
                countryCode = CountryCode.valueOf(configCountryCode);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Invalid country code configured: " + configCountryCode, ex);
            }
        }
        return countryCode;
    }

    private ItemList getItemDetails(List<Long> itemIds) {
        return itemServiceClient.getItemDetails(itemIds);
    }

    @PreAuthorize("hasAnyRole('BUYER', 'SUPPLIER')")
    public InvoiceResponse getInvoiceByBillingId(String email, String billingId) {
        Invoice invoice = invoiceRepository.findByBillingId(billingId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (!invoice.getBuyer().getEmail().equals(email) &&
                !invoice.getSupplier().getEmail().equals(email)) {
            throw new RuntimeException("Access Denied");
        }

        return InvoiceMapper.toInvoiceResponse(invoice);
    }

    @PreAuthorize("hasAnyRole('BUYER', 'SUPPLIER')")
    public Page<InvoiceResponse> getInvoicesForUser(String email, int page, int size) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("invoiceDate").descending());

        Page<Invoice> invoices = null;
        if (user.getRole() == Role.BUYER) {
            invoices = invoiceRepository.findByBuyer(user,pageable);
        } else {
            invoices =  invoiceRepository.findBySupplier(user,pageable);
        }
        List<InvoiceResponse> invoiceResponses = invoices.getContent()
                .stream()
                .map(InvoiceMapper::toInvoiceResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(invoiceResponses, pageable, invoices.getTotalElements());
    }

    @PreAuthorize("hasAnyRole('BUYER', 'SUPPLIER')")
    public Page<InvoiceResponse> getFilteredInvoices(String buyerEmail, String supplierEmail, InvoiceStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("invoiceDate").descending());
        return invoiceRepository.findFilteredInvoices(buyerEmail, supplierEmail, status, pageable)
                .map(InvoiceMapper::toInvoiceResponse);
    }


    @PreAuthorize("hasRole('SUPPLIER')")
    public void softDeleteInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + invoiceId));
        invoice.setStatus(InvoiceStatus.INACTIVE);
        invoiceRepository.save(invoice);
    }


}
