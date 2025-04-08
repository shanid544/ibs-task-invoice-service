package com.ibs_demo.invoice_service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    @NotNull(message = "Billing ID cannot be null")
    @NotBlank(message = "Billing ID cannot be blank")
    private String billingId;

    @NotNull(message = "Buyer ID cannot be null")
    private Long buyerId;

    @NotNull(message = "Billing lines cannot be null")
    @Size(min = 1, message = "At least one billing line must be provided")
    private List<BillingLineRequest> billingLines;

    @NotNull(message = "Payment information is required")
    private PaymentInformationRequest paymentInformation;
}