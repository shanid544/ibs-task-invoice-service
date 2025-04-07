package com.ibs_demo.invoice_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "billing_lines")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonBackReference
    private Invoice invoice;

    private String itemDescription;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;

    public BillingLine(Invoice invoice, String name, Integer integer,  Double unitPrice, double v) {
    }
}
