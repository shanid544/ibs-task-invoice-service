package com.ibs_demo.invoice_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ibs_demo.invoice_service.model.CountryCode;
import com.ibs_demo.invoice_service.model.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String billingId;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private User buyer;  // Buyer is a User

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private User supplier;  // Supplier is a User

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BillingLine> billingLines = new ArrayList<>();

    private LocalDate invoiceDate;
    private LocalDate paymentDueDate;
    private String currencyCode;
    private Double totalInvoiceAmount;
    private Double totalInvoiceAmountDue;

    @Embedded
    private PaymentInformation paymentInformation;

    private String invoiceLanguageCode;

    @Enumerated(EnumType.STRING)
    private CountryCode countryCode;

    private Double tax;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
}
