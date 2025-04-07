package com.ibs_demo.invoice_service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {
    @NotNull
    @NotBlank
    private String supplierEmail;

    @NotNull
    @NotBlank
    private String billingId;

    @NotNull
    private Long buyerId;

    @NotNull
    private List<BillingLineRequest> billingLines;

    @NotNull
    private PaymentInformationRequest paymentInformation;
}