package com.ibs_demo.invoice_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private List<String> details;
}
