package com.ibs_demo.invoice_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInformationResponse {
    private String paymentMethod;
    private String cardType;
    private String paymentInstrumentId;
    private String formOfPaymentId;
    private String cardNumberType;
    private String shortCardNum;
}
