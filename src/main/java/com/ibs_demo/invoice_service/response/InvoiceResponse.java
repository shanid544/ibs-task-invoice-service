package com.ibs_demo.invoice_service.response;

import com.ibs_demo.invoice_service.model.CountryCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {
    private Long id;
    private String billingId;
    private UserResponse buyer;
    private UserResponse supplier;
    private List<BillingLineResponse> billingLines;
    private LocalDate invoiceDate;
    private LocalDate paymentDueDate;
    private String currencyCode;
    private Double totalInvoiceAmount;
    private Double totalInvoiceAmountDue;
    private PaymentInformationResponse paymentInformation;
    private String invoiceLanguageCode;
    private CountryCode countryCode;
    private Double tax;
}
