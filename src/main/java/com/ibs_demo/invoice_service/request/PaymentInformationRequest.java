package com.ibs_demo.invoice_service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInformationRequest {
    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Card type is required when payment method is CARD")
    private String cardType;
    private String paymentInstrumentId;
    private String formOfPaymentId;
    private String cardNumberType;
    private String shortCardNum;
}