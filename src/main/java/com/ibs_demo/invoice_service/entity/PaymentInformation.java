package com.ibs_demo.invoice_service.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInformation {

    private String paymentMethod;  // CREDIT CARD, PAYPAL, etc.
    private String cardType;       // VISA, MasterCard (if applicable)
    private String paymentInstrumentId;  // Unique ID for the payment instrument
    private String formOfPaymentId;      // Payment transaction reference
    private String cardNumberType;       // Masked card number (e.g., 4111XXXXXXXX1234)
    private String shortCardNum;         // Last 4 digits of the card
}
