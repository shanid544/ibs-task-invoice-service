package com.ibs_demo.invoice_service.utils;

import com.ibs_demo.invoice_service.entity.BillingLine;
import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.entity.User;
import com.ibs_demo.invoice_service.request.BillingLineNotificationPayload;
import com.ibs_demo.invoice_service.request.InvoiceNotificationPayload;
import com.ibs_demo.invoice_service.response.BillingLineResponse;
import com.ibs_demo.invoice_service.response.InvoiceResponse;
import com.ibs_demo.invoice_service.response.PaymentInformationResponse;
import com.ibs_demo.invoice_service.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceMapper {

    public static InvoiceResponse toInvoiceResponse(Invoice invoice) {
        if (invoice == null) return null;

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .billingId(invoice.getBillingId())
                .buyer(toUserResponse(invoice.getBuyer()))
                .supplier(toUserResponse(invoice.getSupplier()))
                .billingLines(toBillingLineResponses(invoice.getBillingLines()))
                .invoiceDate(invoice.getInvoiceDate())
                .paymentDueDate(invoice.getPaymentDueDate())
                .currencyCode(invoice.getCurrencyCode())
                .totalInvoiceAmount(invoice.getTotalInvoiceAmount())
                .totalInvoiceAmountDue(invoice.getTotalInvoiceAmountDue())
                .paymentInformation(toPaymentInformationDTO(invoice.getPaymentInformation()))
                .invoiceLanguageCode(invoice.getInvoiceLanguageCode())
                .countryCode(invoice.getCountryCode())
                .tax(invoice.getTax())
                .build();
    }

    private static UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getCountryCode(),
                user.getStatus()
        );
    }

    private static List<BillingLineResponse> toBillingLineResponses(List<BillingLine> billingLines) {
        if (billingLines == null) return null;

        return billingLines.stream()
                .map(line -> new BillingLineResponse(
                        line.getId(),
                        line.getItemDescription(),
                        line.getQuantity(),
                        line.getUnitPrice(),
                        line.getTotalAmount()
                ))
                .collect(Collectors.toList());
    }

    private static PaymentInformationResponse toPaymentInformationDTO(com.ibs_demo.invoice_service.entity.PaymentInformation pi) {
        if (pi == null) return null;

        return new PaymentInformationResponse(
                pi.getPaymentMethod(),
                pi.getCardType(),
                pi.getPaymentInstrumentId(),
                pi.getFormOfPaymentId(),
                pi.getCardNumberType(),
                pi.getShortCardNum()
        );
    }

    public static InvoiceNotificationPayload toNotificationPayload(Invoice invoice) {
        return new InvoiceNotificationPayload(
                invoice.getBillingId(),
                invoice.getBuyer().getEmail(),
                invoice.getSupplier().getEmail(),
                invoice.getCurrencyCode(),
                invoice.getTotalInvoiceAmount(),
                invoice.getTax(),
                invoice.getInvoiceDate(),
                invoice.getPaymentDueDate(),
                convertBillingLines(invoice.getBillingLines())
        );
    }

    // Convert Billing Lines for Notification Payload
    private static List<BillingLineNotificationPayload> convertBillingLines(List<BillingLine> billingLines) {
        return billingLines.stream()
                .map(line -> new BillingLineNotificationPayload(
                        line.getItemDescription(),
                        line.getQuantity(),
                        line.getUnitPrice(),
                        line.getTotalAmount()
                ))
                .collect(Collectors.toList());
    }
}
