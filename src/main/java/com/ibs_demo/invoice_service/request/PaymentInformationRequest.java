package com.ibs_demo.invoice_service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class PaymentInformationRequest {
    @NotNull
    private String paymentMethod;
    @NotNull
    private String cardType;
    private String paymentInstrumentId;
    private String formOfPaymentId;
    private String cardNumberType;
    private String shortCardNum;
}