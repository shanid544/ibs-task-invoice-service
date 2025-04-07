package com.ibs_demo.invoice_service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingLineRequest {
    @NotNull
    private Long itemId;

    @NotNull
    private Integer quantity;
}